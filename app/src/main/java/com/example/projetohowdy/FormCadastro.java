package com.example.projetohowdy;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.utils.Encryption;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.model.Participants;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {
    TextView name, user, email, password;
    Button cadastrar;
    ImageView photo;

    private FirebaseAuth auth;
    private FirebaseStorage storage;

    ActivityResultLauncher<String> mTakePhoto;

    boolean imagemSelecionada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        name = findViewById(R.id.perfilNome);
        user = findViewById(R.id.perfilUsuario);
        email = findViewById(R.id.perfilEmail);
        password = findViewById(R.id.perfilSenha);
        cadastrar = findViewById(R.id.cadastrar);
        photo = findViewById(R.id.profileScreenPhoto);

        storage = FirebaseStorage.getInstance();

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result != null){
                            photo.setImageURI(result);
                            imagemSelecionada = true;
                        }
                    }
                }
        );

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTakePhoto.launch("image/*");
            }
        });

        prepararActionBar();
        acao();
    }

    public void prepararActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Cadastro");
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(FormCadastro.this, FormLogin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FormCadastro.this, FormLogin.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void acao(){
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty() && !user.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && imagemSelecionada == true){
                    User newUser = new User(user.getText().toString(),
                            name.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString());

                    auth = FirebaseConfiguration.getFirebaseAuth();
                    auth.createUserWithEmailAndPassword(newUser.getEmail().toLowerCase(), newUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String encryptedPassword = Encryption.encode64(newUser.getPassword());
                                newUser.setPassword(encryptedPassword);

                                DocumentReference documentReference = FirebaseConfiguration.getFirebaseFirestore().collection("Users").document(auth.getCurrentUser().getUid());
                                documentReference.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Map<String, Participants> participants = new HashMap<>();
                                        Participants sender = new Participants(auth.getCurrentUser().getUid(), 0L, false);
                                        participants.put(FirebaseConfiguration.getFirebaseAuth().getUid(), sender);

                                        Map inbox = new HashMap<>();
                                        inbox.put("participants", participants);

                                        FirebaseConfiguration.getFirebaseFirestore().collection("Inbox").add(inbox).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                StorageReference reference = storage.getReference().child("upload").child("images");
                                                StorageReference nameImage = reference.child(auth.getCurrentUser().getUid() + ".jpg");

                                                BitmapDrawable drawable = (BitmapDrawable) photo.getDrawable();
                                                Bitmap bitmap = drawable.getBitmap();
                                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                                                UploadTask uploadTask = nameImage.putBytes(bytes.toByteArray());
                                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        Toast.makeText(FormCadastro.this, "Sucesso ao cadastrar novo usuário!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(FormCadastro.this, FormLogin.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        });
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
                }else{
                    Toast.makeText(FormCadastro.this, "Por favor, preencha todos os campos, inclusive selecione a foto de perfil", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}