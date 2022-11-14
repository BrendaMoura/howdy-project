package com.example.projetohowdy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TelaConversa extends AppCompatActivity {
    View novaconversa;

    private ImageView image_tela_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_conversa);
        getSupportActionBar().hide();

        novaconversa = findViewById(R.id.novaconversa);

        acao();

        IniciarPefil();

        image_tela_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPerfil = new Intent(TelaConversa.this,TelaPerfilUsuario.class);
                startActivity(intentPerfil);
            }
        });
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

    private void IniciarPefil(){
        image_tela_perfil = findViewById(R.id.perfil);
    }
}