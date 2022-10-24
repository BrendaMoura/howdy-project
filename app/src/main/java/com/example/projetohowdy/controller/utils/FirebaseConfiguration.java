package com.example.projetohowdy.controller.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConfiguration {
    private static FirebaseAuth auth;
    private static FirebaseFirestore db;

    // return FirebaseAuth instance
    // authentication
    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    // return Firebase Firestore instance
    public static FirebaseFirestore getFirebaseFirestore() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

    public boolean connectUser(String email, String senha) {

        getFirebaseAuth().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            result = true;
                        }
                    }
                });

        return result;
    }

    public boolean disconnectUser() {
        try {
            auth.signOut();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
