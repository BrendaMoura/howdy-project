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

    public String createUser (User user) {
        auth = FirebaseConfiguration.getFirebaseAuth();

        auth.createUserWithEmailAndPassword(user.email.toLowerCase(), user.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String encryptedPassword = Encryption.encode64(user.getPassword());
                    user.setPassword(encryptedPassword);

                    DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            System.out.println("Success");
                            finalMessage = "Sucesso ao cadastrar!";
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finalMessage = "Erro ao cadastrar!";
                        }
                    });
                }else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        finalMessage = "Por favor, tente uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        finalMessage = "Email inválido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        finalMessage = "Conta já cadastrada!";
                    } catch (Exception e) {
                        e.printStackTrace();
                        finalMessage = "Erro ao cadastrar!";
                    }
                }
            }
        });

        return finalMessage;
    }

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
