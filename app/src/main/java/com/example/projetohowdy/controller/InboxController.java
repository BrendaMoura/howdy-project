package com.example.projetohowdy.controller;

import androidx.annotation.NonNull;

import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Inbox;
import com.example.projetohowdy.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class InboxController {
    private FirebaseAuth auth;
    private String finalMessage = "No response yet";
    private static User receiver;
    private static Inbox inbox;

    public static User getReceiver() {
        return receiver;
    }

    public static void setReceiver(User receiver) {
        InboxController.receiver = receiver;
    }

    public static Inbox getInbox() {
        return inbox;
    }

    public static void setInbox(Inbox inbox) {
        InboxController.inbox = inbox;
    }

    public static void setIdInbox(String id) {
        InboxController.inbox.setIdInbox(id);
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
