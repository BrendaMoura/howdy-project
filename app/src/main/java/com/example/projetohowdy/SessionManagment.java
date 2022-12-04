package com.example.projetohowdy;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.projetohowdy.model.User;

public class SessionManagment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManagment(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user){
        int id = user.getId();
        editor.putInt(SESSION_KEY,id).commit();
    }

    public int getSession(){
        return -1;
    }
}
