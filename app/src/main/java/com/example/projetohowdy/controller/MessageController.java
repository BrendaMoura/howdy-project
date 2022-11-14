package com.example.projetohowdy.controller;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projetohowdy.TelaBatePapo;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class MessageController {
    private String finalMessage = "No response yet";

    public void updateMessage(Message message){
        Task<Void> documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Message").document(message.idMessage).set(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // Alterada
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Falha
            }
        });
    }

    public void forwardMessage(Message message){
        Task<DocumentReference> documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Message").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Encaminhada
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Falha
            }
        });
    }
}
