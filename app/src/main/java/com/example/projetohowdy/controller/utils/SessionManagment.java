package com.example.projetohowdy.controller.utils;

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

    public void startUserSession(User user){
        String id = user.getIdUSer();
        editor.putString(SESSION_KEY, id);
        editor.commit();
    }

    public String getSession(){
        return sharedPreferences.getString(SESSION_KEY, "no_user");
    }

    public  void endUserSession(){
        editor.clear();
        editor.commit();
    }
}
