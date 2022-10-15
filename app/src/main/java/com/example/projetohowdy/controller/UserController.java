package com.example.projetohowdy.controller;

import androidx.annotation.NonNull;

import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.controller.utils.Encryption;
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

public class UserController {
    private FirebaseAuth auth;
    private String finalMessage = "No response yet";

    public String updateAccount(User user){
        auth = FirebaseConfiguration.getFirebaseAuth();
        String email = auth.getCurrentUser().getEmail();

        DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                try{
                    if( email != user.email){
                        auth.getCurrentUser().updateEmail(user.email);
                    }

                    auth.getCurrentUser().updatePassword(user.password);
                    finalMessage = "Sucesso ao alterar conta!";
                }catch (Exception e){
                    e.printStackTrace();
                    finalMessage = "Erro ao alterar conta!";
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finalMessage = "Erro ao alterar conta!";
            }
        });

        return finalMessage;
    }
}