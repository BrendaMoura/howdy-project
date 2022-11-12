package com.example.projetohowdy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projetohowdy.controller.utils.FirebaseConfiguration;

public class FormLogin extends AppCompatActivity {
    TextView email, senha, cadastro;
    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        email = findViewById(R.id.cadastroemail);
        senha = findViewById(R.id.cadastrosenha);
        entrar = findViewById(R.id.login);
        cadastro = findViewById(R.id.cadastro);

        acao();

    }

    public void acao(){
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseConfiguration firebaseConfiguration = new FirebaseConfiguration();
                firebaseConfiguration.connectUser(email.getText().toString(), senha.getText().toString());
            }
        });

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });
    }
}