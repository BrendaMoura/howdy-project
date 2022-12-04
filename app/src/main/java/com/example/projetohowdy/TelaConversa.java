package com.example.projetohowdy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.InboxController;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.controller.utils.SessionManagment;
import com.example.projetohowdy.model.Inbox;
import com.example.projetohowdy.model.Message;
import com.example.projetohowdy.model.Participants;
import com.example.projetohowdy.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TelaConversa extends AppCompatActivity {
    String id;
    FloatingActionButton novaconversa;
    RecyclerView rv;

    FirebaseStorage storage;

    GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_conversa);

        id = FirebaseConfiguration.getFirebaseAuth().getCurrentUser().getUid();

        storage = FirebaseStorage.getInstance();

        novaconversa = findViewById(R.id.novaconversa);
        rv = findViewById(R.id.recycle_inbox);

        adapter = new GroupAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        prepararActionBar();
        getInboxUsers();
        acao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void prepararActionBar(){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Howdy");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_profile:
                Intent intent = new Intent(TelaConversa.this, TelaPerfilUsuario.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Realmente deseja sair do app?")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create().show();
    }

    public void getInboxUsers(){
        CollectionReference collectionReference = FirebaseConfiguration.getFirebaseFirestore().collection("Inbox");
        String campo = "participants." + id + ".idParticipant";
        collectionReference.whereEqualTo(campo, id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<Inbox> inboxes = queryDocumentSnapshots.toObjects(Inbox.class);

                    for (int i = 0; i<inboxes.size(); i++){
                        inboxes.get(i).setIdInbox(queryDocumentSnapshots.getDocuments().get(i).getId());
                        if(inboxes.get(i).getParticipants().size() == 1){
                           InboxController.setChatPessoal(inboxes.get(i));
                        }

                        adapter.add(new InboxItem(inboxes.get(i)));
                    }

                    InboxController.setInboxes(inboxes);
                }
            }
        });
    }

    public void acao(){
        novaconversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InboxController.setInbox(null);
                Intent intent = new Intent(TelaConversa.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class InboxItem extends Item<GroupieViewHolder>{
        private Inbox inbox;
        private User receiver;

        public InboxItem(Inbox inbox) {
            this.inbox = inbox;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            String idReceiver = "";
            TextView name = viewHolder.itemView.findViewById(R.id.nameInboxUser);
            TextView lastMessage = viewHolder.itemView.findViewById(R.id.last_msg);
            ImageView profilePhoto = viewHolder.itemView.findViewById(R.id.img_user_inbox);

            if(inbox.getParticipants().size() == 1){
                idReceiver = id;
            }else{
                for (Map.Entry<String, Participants> participant : inbox.getParticipants().entrySet()) {
                    if(!participant.getKey().equals(id)){
                        idReceiver = participant.getValue().getIdParticipant();
                        break;
                    }
                }
            }

            profilePhoto.setImageResource(R.drawable.ic_no_picture);

            StorageReference reference = storage.getReference("upload/images/" + idReceiver + ".jpg");

            try {
                File localfile = File.createTempFile("tempfile", ".jpg");
                reference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot != null){
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            profilePhoto.setImageBitmap(bitmap);
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(idReceiver).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    receiver = documentSnapshot.toObject(User.class);

                    name.setText(documentSnapshot.getString("name"));
                    if(inbox.getIdLastMessage() != null){
                        FirebaseConfiguration.getFirebaseFirestore().collection("Message").document(inbox.getIdLastMessage()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(value != null){
                                    lastMessage.setText(value.getString("content"));
                                }
                            }
                        });
                    }
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InboxController.setReceiver(receiver);
                    InboxController.setInbox(inbox);
                    Intent intent = new Intent(TelaConversa.this, TelaBatePapo.class);
                    startActivity(intent);
                    finish();
                }
            });

        }

        @Override
        public int getLayout() {
            return R.layout.item_inbox;
        }
    }
}