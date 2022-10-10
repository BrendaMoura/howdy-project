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

    public String deleteUser () {
        try{
            auth = FirebaseConfiguration.getFirebaseAuth();

            DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
            documentReference.delete();

            auth.getCurrentUser().delete();

            finalMessage = "Conta excluída com sucesso!";
        }catch (Exception e){
            finalMessage = "Erro ao excluir conta!";
        }

        return finalMessage;
    }
}
