package com.example.projetohowdy.controller;

import androidx.annotation.NonNull;

import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Inbox;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InboxController {
    private FirebaseAuth auth;
    private String finalMessage = "No response yet";

    //We have to get the inboxes that have a match with idInitiator of idReceiver
    //We have to transform the result in a list of objects
    public ArrayList<Inbox> findAllInboxes(){
        ArrayList<Inbox> listInbox = new ArrayList<>();;
        auth = FirebaseConfiguration.getFirebaseAuth();

        CollectionReference collectionReference = FirebaseConfiguration.getFirebaseFirestore().collection("Inbox");
        collectionReference.whereEqualTo("idInitiator", "7OcH06PjFYNpE5h4bWR8v7ts2bs1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Inbox> inboxes = queryDocumentSnapshots.toObjects(Inbox.class);

                //System.out.println("teste: " + inboxes.get(1));
                listInbox.addAll(inboxes);
                //System.out.println("Lista: "+ listInbox.get(1).getIdInitiator());
            }
        });

        return listInbox;
    }

    public void findInbox(){
        CollectionReference collectionReference = FirebaseConfiguration.getFirebaseFirestore().collection("Inbox");
        collectionReference.whereEqualTo("idInitiator", "id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //result
                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });
        collectionReference.whereEqualTo("idReceiver", "id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //result
                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });
    }

    //We have to verify if the inbox already exists before create another one
    public String createInbox(Inbox inbox){
        Task<DocumentReference> documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").add(inbox).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finalMessage = "Caixa de inbox criada com sucesso!";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finalMessage = "Falha ao criar caixa de inbox!";
            }
        });

        return finalMessage;
    }

    public String deleteInbox(String idInbox){
        FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").document(idInbox).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finalMessage = "Inbox excluída com sucesso!";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finalMessage = "Falha ao excluir inbox!";
            }
        });

        return finalMessage;
    }
}
