package com.example.projetohowdy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.InboxController;
import com.example.projetohowdy.controller.MessageController;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Inbox;
import com.example.projetohowdy.model.Message;
import com.example.projetohowdy.model.Participants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaBatePapo extends AppCompatActivity {
    private TextView content;
    private Button enviar;
    private GroupAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bate_papo);

        content = findViewById(R.id.mensagem);
        enviar = findViewById(R.id.enviarmensagem);
        rv = findViewById(R.id.recycler_chat);

        prepararActionBar();

        adapter = new GroupAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        getMessages();
        acao();
    }

    public void prepararActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(InboxController.getReceiver().getName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(TelaBatePapo.this, TelaConversa.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getMessages(){
        String idInbox = "teste";

        if(InboxController.getInbox() != null){
            idInbox = InboxController.getInbox().getIdInbox();
        }

        FirebaseConfiguration.getFirebaseFirestore().collection("Message").whereEqualTo("idInbox", idInbox).orderBy("createdAt", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error == null){
                    List<DocumentChange> documentChanges = value.getDocumentChanges();

                    for (DocumentChange doc: documentChanges) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            Message message = doc.getDocument().toObject(Message.class);
                            message.setIdMessage(doc.getDocument().getId());
                            adapter.add(new MessageItem(message));
                        }
                    }
                }
            }
        });
    }

    public void acao(){
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!content.getText().toString().isEmpty()){
                    Date timestamp = new Date(System.currentTimeMillis());
                    if(InboxController.getInbox() != null){
                        Map message = new HashMap<>();
                        message.put("idInbox", InboxController.getInbox().getIdInbox());
                        message.put("idSender", FirebaseConfiguration.getFirebaseAuth().getCurrentUser().getUid());
                        message.put("content", content.getText().toString());
                        message.put("createdAt", timestamp);
                        message.put("updatedAt", timestamp);
                        message.put("deletedBySender", false);
                        message.put("deletedByReceiver", false);

                        FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                content.setText("");
                                Map<String, String> lastMessage = new HashMap<>();
                                lastMessage.put("idLastMessage", documentReference.getId());
                                FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").document(InboxController.getInbox().getIdInbox()).set(lastMessage, SetOptions.merge());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TelaBatePapo.this, "Falha ao enviar mensagem, tente novamente!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Map<String, Participants> participants = new HashMap<>();
                        Participants receiver = new Participants(InboxController.getReceiver().idUSer, 1L, false);
                        Participants sender = new Participants(FirebaseConfiguration.getFirebaseAuth().getUid(), 0L, false);
                        participants.put(InboxController.getReceiver().idUSer, receiver);
                        participants.put(FirebaseConfiguration.getFirebaseAuth().getUid(), sender);

                        Map inbox = new HashMap<>();
                        inbox.put("participants", participants);

                        FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").add(inbox).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Inbox newInbox = new Inbox(documentReference.getId(), "", participants);
                                InboxController.setInbox(newInbox);

                                Map message = new HashMap<>();
                                message.put("idInbox", documentReference.getId());
                                message.put("idSender", FirebaseConfiguration.getFirebaseAuth().getCurrentUser().getUid());
                                message.put("content", content.getText().toString());
                                message.put("createdAt", timestamp);
                                message.put("updatedAt", timestamp);
                                message.put("deletedBySender", false);
                                message.put("deletedByReceiver", false);


                                FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference2) {
                                        content.setText("");
                                        Map<String, String> lastMessage = new HashMap<>();
                                        lastMessage.put("idLastMessage", documentReference2.getId());

                                        FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").document(documentReference.getId()).set(lastMessage, SetOptions.merge());

                                        getMessages();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TelaBatePapo.this, "Falha ao enviar mensagem, tente novamente!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TelaBatePapo.this , "Erro ao enviar mensagem, tente novamente!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private class MessageItem extends Item<GroupieViewHolder> {

        private final Message message;

        private MessageItem(Message message) {
            this.message = message;
        }

        @Override
        public void bind(GroupieViewHolder viewHolder, int position) {
            TextView txtMessage;
            if(message.getIdSender().equals(FirebaseConfiguration.getFirebaseAuth().getUid())){
                txtMessage = viewHolder.itemView.findViewById(R.id.txt_message_from);
            }else{
                txtMessage = viewHolder.itemView.findViewById(R.id.txt_message_to);
            }

            txtMessage.setText(message.getContent());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(TelaBatePapo.this, viewHolder.itemView);

                    if(message.getIdSender().equals(FirebaseConfiguration.getFirebaseAuth().getUid())){
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_sender, popupMenu.getMenu());
                    }else{
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_receiver, popupMenu.getMenu());
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Date timestamp = new Date(System.currentTimeMillis());
                            if (menuItem.getTitle().equals("Editar")) {
                                AlertDialog dialog;
                                EditText editText;

                                dialog = new AlertDialog.Builder(viewHolder.itemView.getContext()).create();
                                editText = new EditText(viewHolder.itemView.getContext());

                                dialog.setTitle("Edite a mensagem");
                                dialog.setView(editText);

                                editText.setText(message.getContent());

                                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Salvar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        message.setContent(editText.getText().toString());
                                        message.setUpdatedAt(timestamp);

                                        FirebaseConfiguration.getFirebaseFirestore().collection("Message").document(message.getIdMessage()).set(message, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                txtMessage.setText(message.getContent());
                                                dialog.cancel();
                                                popupMenu.dismiss();
                                            }
                                        });
                                    }
                                });

                                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.cancel();
                                        popupMenu.dismiss();
                                    }
                                });

                                dialog.show();
                            }
                            else if(menuItem.getTitle().equals("Deletar")){

                                message.setContent("Essa mensagem foi apagada");
                                message.setUpdatedAt(timestamp);

                                if (message.getIdSender().equals(FirebaseConfiguration.getFirebaseAuth().getUid())) {
                                    message.setDeletedBySender(true);
                                } else {
                                    message.setDeletedByReceiver(true);
                                }

                                FirebaseConfiguration.getFirebaseFirestore().collection("Message").document(message.getIdMessage()).set(message, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        txtMessage.setText("Essa mensagem foi apagada");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TelaBatePapo.this, "Erro ao apagar mensagem, tente novamente!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                MessageController.setForwardMessage(message);
                                Intent intent = new Intent(TelaBatePapo.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        @Override
        public int getLayout() {
            return message.getIdSender().equals(FirebaseConfiguration.getFirebaseAuth().getUid()) ? R.layout.item_from_message : R.layout.item_to_message;
        }
    }

}