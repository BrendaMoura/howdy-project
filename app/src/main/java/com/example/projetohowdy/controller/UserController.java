package com.example.projetohowdy.controller;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projetohowdy.FormLogin;
import com.example.projetohowdy.TelaPerfilUsuario;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.controller.utils.Encryption;
import com.example.projetohowdy.controller.utils.Session;
import com.example.projetohowdy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class UserController {
    private FirebaseAuth auth;

    public void updateAccount(User user) {
        auth = FirebaseConfiguration.getFirebaseAuth();
        String email = auth.getCurrentUser().getEmail();

        DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                try {
                    if (email != user.email) {
                        auth.getCurrentUser().updateEmail(user.email);
                    }

                    auth.getCurrentUser().updatePassword(user.password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //
            }
        });
    }

    public void deleteUser() {
        try {
            auth = FirebaseConfiguration.getFirebaseAuth();

            DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
            documentReference.delete();

        } catch (Exception e) {
            //
        }
    }

    public void blockUser(String id){
        auth = FirebaseConfiguration.getFirebaseAuth();
        List<String> updatedBlockedPeople = Session.user.blockedPeople;
        if(!updatedBlockedPeople.contains(id)){
            updatedBlockedPeople.add(id);
            DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
            documentReference.update("blockedPeople", updatedBlockedPeople);
        }else{
            //usuário já bloqueado
        }
    }

    public void unBlockUser(String id){
        List<String> updatedBlockedPeople = Session.user.blockedPeople;
        if(updatedBlockedPeople.contains(id)){
            updatedBlockedPeople.remove(id);
            DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
            documentReference.update("blockedPeople", updatedBlockedPeople);
        }else{
            //usuário já bloqueado
        }
    }

}
