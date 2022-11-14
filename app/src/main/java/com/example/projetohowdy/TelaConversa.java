package com.example.projetohowdy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaConversa extends AppCompatActivity {
    View novaconversa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_conversa);

        novaconversa = findViewById(R.id.novaconversa);

        acao();
    }

    public void acao(){
        novaconversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaConversa.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}