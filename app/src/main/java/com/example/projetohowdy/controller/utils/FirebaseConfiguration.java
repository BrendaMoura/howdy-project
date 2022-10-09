package com.example.projetohowdy.controller.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConfiguration {
    private static FirebaseAuth auth;
    private static FirebaseFirestore db;

    //return FirebaseAuth instance
    //authentication
    public static FirebaseAuth getFirebaseAuth() {
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    //return Firebase Firestore instance
    public static FirebaseFirestore getFirebaseFirestore(){
        if(db == null){
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

}
