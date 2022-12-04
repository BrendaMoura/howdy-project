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
import com.example.projetohowdy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.DocumentSnapshot;

public class FormLogin extends AppCompatActivity {
    TextView email, senha, cadastro;
    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar().hide();

        email = findViewById(R.id.perfilEmail);
        senha = findViewById(R.id.perfilSenha);
        entrar = findViewById(R.id.login);
        cadastro = findViewById(R.id.cadastro);

        acao();

    }

    public void acao(){

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().toString().isEmpty() && !senha.getText().toString().isEmpty()){
                    FirebaseConfiguration.getFirebaseAuth().signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(FirebaseConfiguration.getFirebaseAuth().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user = documentSnapshot.toObject(User.class);
                                        user.setIdUSer(FirebaseConfiguration.getFirebaseAuth().getUid());
                                        Session.user = user;
                                        Intent intent = new Intent(FormLogin.this, TelaConversa.class);
                                        //Intent intent = new Intent(FormLogin.this, Image_Profile.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }else{
                                try {
                                    throw task.getException();
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    Toast.makeText(FormLogin.this, "E-mail ou senha incorretos, tente novamente!", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    Toast.makeText(FormLogin.this, "Falha ao logar, tente novamente!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
        @Override
        protected void onStart(){
            super.onStart();
        }

        SessionManagment sessionManagment = new SessionManagment(FormLogin.this);
        sessionManagment.saveSession(user);

        moveToMainActivity();

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });
    }

    private void moveToMainActivity() {
        Intent intent = new Intent (FormLogin.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}