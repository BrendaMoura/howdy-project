package com.example.projetohowdy.controller.utils;

import android.util.Base64;

public class Encryption {
    public static String encode64(String txt){
        return  Base64.encodeToString(txt.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)", "");

    }
    public static String decode64(String txt){
        return new String (Base64.decode(txt, Base64.DEFAULT));
    }
}
