package com.example.projetohowdy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projetohowdy.controller.UserController;
import com.example.projetohowdy.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}