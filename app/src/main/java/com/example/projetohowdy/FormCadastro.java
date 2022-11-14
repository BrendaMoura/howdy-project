package com.example.projetohowdy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.UserController;
import com.example.projetohowdy.controller.utils.Encryption;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {
    TextView name, user, email, password;
    Button cadastrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();

        name = findViewById(R.id.cadastroname);
        user = findViewById(R.id.cadastrouser);
        email = findViewById(R.id.cadastroemail);
        password = findViewById(R.id.cadastrosenha);
        cadastrar = findViewById(R.id.cadastrar);

        acao();
    }

    public void acao(){
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> newUser = new HashMap<>();
                newUser.put("user", user.getText().toString());
                newUser.put("name", name.getText().toString());
                newUser.put("email", email.getText().toString());
                newUser.put("password", password.getText().toString());

                auth = FirebaseConfiguration.getFirebaseAuth();
                auth.createUserWithEmailAndPassword(newUser.get("email").toLowerCase(), newUser.get("password")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String encryptedPassword = Encryption.encode64(newUser.get("password"));
                            newUser.put("password", encryptedPassword);

                            DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
                            documentReference.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(FormCadastro.this, "Sucesso ao cadastrar novo usuário!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(FormCadastro.this, FormLogin.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FormCadastro.this, "Erro ao cadastrar novo usuário!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(FormCadastro.this, "Por favor, tente uma senha mais forte!", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(FormCadastro.this, "Email inválido!", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(FormCadastro.this, "Conta já cadastrada!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(FormCadastro.this, "Erro ao cadastrar!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}