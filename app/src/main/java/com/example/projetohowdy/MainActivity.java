package com.example.projetohowdy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.InboxController;
import com.example.projetohowdy.controller.MessageController;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.controller.utils.Session;
import com.example.projetohowdy.model.Inbox;
import com.example.projetohowdy.model.Participants;
import com.example.projetohowdy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.xwray.groupie.GroupAdapter;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    String id;
    TextView usuario;
    Button pesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.usuario);
        pesquisar = findViewById(R.id.pesquisar);

        id = FirebaseConfiguration.getFirebaseAuth().getCurrentUser().getUid();

        prepararActionBar();
        acao();
    }

    public void prepararActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Pesquisar usuário");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MainActivity.this, TelaConversa.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void acao(){
        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date timestamp = new Date(System.currentTimeMillis());

                if(MessageController.getForwardMessage() != null && usuario.getText().toString().equals(Session.user.user)){
                    System.out.println("Teste 333");
                    MessageController.getForwardMessage().setCreatedAt(timestamp);

                    MessageController.getForwardMessage().setIdInbox(InboxController.getChatPessoal().getIdInbox());
                    FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(MessageController.getForwardMessage()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "Mensagem encaminhada com sucesso!", Toast.LENGTH_SHORT).show();
                            InboxController.setInbox(InboxController.getChatPessoal());

                            Map<String, String> lastMessage = new HashMap<>();
                            lastMessage.put("idLastMessage", documentReference.getId());
                            FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").document(InboxController.getInbox().getIdInbox()).set(lastMessage, SetOptions.merge());

                            MessageController.setForwardMessage(null);
                            Intent intent = new Intent(MainActivity.this, TelaBatePapo.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            MessageController.setForwardMessage(null);
                            Toast.makeText(MainActivity.this, "Erro ao encaminhar mensagem, tente novamente", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                else{
                    Task<QuerySnapshot> documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").whereEqualTo("user", usuario.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this , "Erro ao buscar usuário, tente novamente!", Toast.LENGTH_SHORT).show();
                            }else if(task.getResult().getDocuments().isEmpty()){
                                Toast.makeText(MainActivity.this , "Usuário não existe, tente novamente!", Toast.LENGTH_SHORT).show();
                            }else{
                                List<User> receiver = task.getResult().toObjects(User.class);
                                receiver.get(0).setIdUSer(task.getResult().getDocuments().get(0).getId());
                                InboxController.setReceiver(receiver.get(0));

                                CollectionReference collectionReference = FirebaseConfiguration.getFirebaseFirestore().collection("Inbox");
                                String idSender = "participants." + id + ".idParticipant";
                                String idReceiver = "participants." + task.getResult().getDocuments().get(0).getId() + ".idParticipant";
                                collectionReference.whereEqualTo(idSender, id).whereEqualTo(idReceiver, task.getResult().getDocuments().get(0).getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<Inbox> inboxes = queryDocumentSnapshots.toObjects(Inbox.class);
                                        if(inboxes.isEmpty() && MessageController.getForwardMessage() != null){
                                            System.out.println("Teste 222");
                                            Map<String, Participants> participants = new HashMap<>();
                                            Participants receiver = new Participants(task.getResult().getDocuments().get(0).getId(), 1L, false);
                                            Participants sender = new Participants(id, 0L, false);
                                            participants.put(task.getResult().getDocuments().get(0).getId(), receiver);
                                            participants.put(FirebaseConfiguration.getFirebaseAuth().getUid(), sender);

                                            Map inbox = new HashMap<>();
                                            inbox.put("participants", participants);

                                            FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").add(inbox).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    MessageController.getForwardMessage().setIdInbox(documentReference.getId());
                                                    FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(MessageController.getForwardMessage()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            MessageController.setForwardMessage(null);
                                                            Toast.makeText(MainActivity.this, "Mensagem encaminhada com sucesso!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        else if(!inboxes.isEmpty()){
                                            if(MessageController.getForwardMessage() != null){
                                                System.out.println("Teste 111");
                                                MessageController.getForwardMessage().setIdInbox(queryDocumentSnapshots.getDocuments().get(0).getId());
                                                FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(MessageController.getForwardMessage()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Map<String, String> lastMessage = new HashMap<>();
                                                        lastMessage.put("idLastMessage", documentReference.getId());
                                                        FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").document(InboxController.getInbox().getIdInbox()).set(lastMessage, SetOptions.merge());
                                                        MessageController.setForwardMessage(null);
                                                        Toast.makeText(MainActivity.this, "Mensagem encaminhada com sucesso!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        MessageController.setForwardMessage(null);
                                                        Toast.makeText(MainActivity.this, "Erro ao encaminhar mensagem, tente novamente", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                            }
                                            InboxController.setInbox(inboxes.get(0));
                                            InboxController.setIdInbox(queryDocumentSnapshots.getDocuments().get(0).getId());
                                        }
                                        else{
                                            InboxController.setInbox(null);
                                        }

                                        Intent intent = new Intent(MainActivity.this, TelaBatePapo.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        }
                    });

                }

            }
        });
    }
}