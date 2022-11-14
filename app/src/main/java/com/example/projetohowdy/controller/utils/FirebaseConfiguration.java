package com.example.projetohowdy.controller.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConfiguration {
    private static FirebaseAuth auth;
    private static FirebaseFirestore db;
    private static boolean result;

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
