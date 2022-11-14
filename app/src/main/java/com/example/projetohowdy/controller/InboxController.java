package com.example.projetohowdy.controller;

import androidx.annotation.NonNull;

import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.controller.utils.Session;
import com.example.projetohowdy.model.Inbox;
import com.example.projetohowdy.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

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

    public void pinInbox(String id){
        List<String> updatedPinnedInboxes = Session.user.pinnedInboxes;
        if(!updatedPinnedInboxes.contains(id)){
            updatedPinnedInboxes.add(id);
            DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
            documentReference.update("pinnedInboxes", updatedPinnedInboxes).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    // usuario bloqueado
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Erro
                }
            });
        }else{
            //inbox já pinadas
        }
    }

    public void unPinInbox(String id){
        List<String> updatedPinnedInboxes = Session.user.pinnedInboxes;
        if(updatedPinnedInboxes.contains(id)){
            updatedPinnedInboxes.remove(id);
            DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
            documentReference.update("pinnedInboxes", updatedPinnedInboxes).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    // usuario bloqueado
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Erro
                }
            });
        }else{
            //inbox já pinadas
        }
    }
}
