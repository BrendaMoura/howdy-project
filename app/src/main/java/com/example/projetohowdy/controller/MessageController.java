package com.example.projetohowdy.controller;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projetohowdy.TelaBatePapo;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MessageController {
    private static Message forwardMessage;

    public static Message getForwardMessage() {
        return forwardMessage;
    }

    public static void setForwardMessage(Message forwardMessage) {
        MessageController.forwardMessage = forwardMessage;
    }
}
