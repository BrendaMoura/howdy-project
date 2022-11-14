package com.example.projetohowdy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.InboxController;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Inbox;
import com.example.projetohowdy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;
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

        acao();
    }

    public void acao(){
        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task<QuerySnapshot> documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").whereEqualTo("user", usuario.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this , "Erro ao buscar usuário, tente novamente!", Toast.LENGTH_SHORT).show();
                        }else if(task.getResult().getDocuments().isEmpty()){
                            Toast.makeText(MainActivity.this , "Usuário não existe, tente novamente!", Toast.LENGTH_SHORT).show();
                        }else{
                            // Salva as informações do receiver
                            List<User> receiver = task.getResult().toObjects(User.class);
                            receiver.get(0).setIdUSer(task.getResult().getDocuments().get(0).getId());
                            InboxController.setReceiver(receiver.get(0));

                            CollectionReference collectionReference = FirebaseConfiguration.getFirebaseFirestore().collection("Inbox");
                            //.whereEqualTo("info", task.getResult().getDocuments().get(0).getId())
                            String campo = "participants." + id + ".idParticipant";
                            collectionReference.whereEqualTo(campo, id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<Inbox> inboxes = queryDocumentSnapshots.toObjects(Inbox.class);
                                    if(inboxes.isEmpty()){
                                        InboxController.setInbox(null);
                                    }else{
                                        InboxController.setInbox(inboxes.get(0));
                                        InboxController.setIdInbox(queryDocumentSnapshots.getDocuments().get(0).getId());

                                        // System.out.println("Teste: " + InboxController.getInbox().getInfo());
                                        // System.out.println("Id teste: " + InboxController.getInbox().getIdInbox());
                                        // System.out.println("Teste: " + inboxes.get(0).getInfo().get("dKAuELiEShhv05Y5Np0WhSoVjoq1").getUnSeenQuant());
                                    }

                                    Intent intent = new Intent(MainActivity.this, TelaBatePapo.class);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                });
            }
        });
    }
}