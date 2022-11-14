package com.example.projetohowdy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.InboxController;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Inbox;
import com.example.projetohowdy.model.Participants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TelaBatePapo extends AppCompatActivity {
    TextView content;
    Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bate_papo);

        content = findViewById(R.id.mensagem);
        enviar = findViewById(R.id.enviarmensagem);

        acao();
    }

    public void acao(){
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if(InboxController.getInbox() != null){
                    Map message = new HashMap<>();
                    message.put("idInbox", InboxController.getInbox().getIdInbox());
                    message.put("idSender", FirebaseConfiguration.getFirebaseAuth().getCurrentUser().getUid());
                    message.put("content", content.getText().toString());
                    message.put("createdAt", timestamp);
                    message.put("updatedAt", timestamp);
                    message.put("deletedBySender", false);
                    message.put("deletedByReceiver", false);

                    Task<DocumentReference> documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Mensagem enviada com sucesso
                            // Atualizar lista de mensagem
                            Toast.makeText(TelaBatePapo.this, "Mensagem enviada com sucesso!", Toast.LENGTH_SHORT).show();
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

                    Task<DocumentReference> documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").add(inbox).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Inbox finalInbox = new Inbox(documentReference.getId(), "", participants);
                            InboxController.setInbox(finalInbox);

                            Map message = new HashMap<>();
                            message.put("idInbox", documentReference.getId());
                            message.put("idSender", FirebaseConfiguration.getFirebaseAuth().getCurrentUser().getUid());
                            message.put("content", content.getText().toString());
                            message.put("createdAt", timestamp);
                            message.put("updatedAt", timestamp);
                            message.put("deletedBySender", false);
                            message.put("deletedByReceiver", false);


                            Task<DocumentReference> documentReference2 = FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //Mensagem enviada com sucesso
                                    // Atualizar lista de mensagem
                                    Toast.makeText(TelaBatePapo.this, "Mensagem enviada com sucesso!", Toast.LENGTH_SHORT).show();
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
        });
    }
}