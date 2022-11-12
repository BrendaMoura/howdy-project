package com.example.projetohowdy.controller;

import androidx.annotation.NonNull;

import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class MessageController {
    private String finalMessage = "No response yet";

    public String sendMessage(Message message){
        Task<DocumentReference> documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finalMessage = "Mensagem enviada com sucesso!";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finalMessage = "Falha ao enviar mensagem!";
            }
        });

        return finalMessage;
    }
}
