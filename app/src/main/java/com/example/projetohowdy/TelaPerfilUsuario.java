package com.example.projetohowdy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.controller.utils.Session;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

public class TelaPerfilUsuario extends AppCompatActivity {
    View deslogar;
    TextView username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil_usuario);
        getSupportActionBar().hide();

        deslogar = findViewById(R.id.deslogar);
        username = findViewById(R.id.perfilUsuario);
        email = findViewById(R.id.perfilEmail);

        username.setText(Session.user.user);
        email.setText(Session.user.email);

        acao();
    }

    public void acao(){
        deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseConfiguration firebaseConfiguration = new FirebaseConfiguration();
                if(firebaseConfiguration.disconnectUser()){
                    Toast.makeText(TelaPerfilUsuario.this, "Usuário deslogado!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TelaPerfilUsuario.this, FormLogin.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(TelaPerfilUsuario.this, "Erro ao deslogar, tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}