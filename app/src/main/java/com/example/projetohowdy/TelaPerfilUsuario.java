package com.example.projetohowdy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetohowdy.controller.utils.Encryption;
import com.example.projetohowdy.controller.utils.FirebaseConfiguration;
import com.example.projetohowdy.controller.utils.Session;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class TelaPerfilUsuario extends AppCompatActivity {
    View deslogar;
    TextView name, username, email, password;
    ImageView profile;

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil_usuario);

        deslogar = findViewById(R.id.deslogar);
        name = findViewById(R.id.perfilNome);
        username = findViewById(R.id.perfilUsuario);
        email = findViewById(R.id.perfilEmail);
        password = findViewById(R.id.perfilSenha);
        profile = findViewById(R.id.profileScreenPhoto);

        storage = FirebaseStorage.getInstance();

        name.setText(Session.user.name);
        username.setText(Session.user.user);
        email.setText(Session.user.email);
        password.setText(Encryption.decode64(Session.user.password));

        prepararActionBar();
        getProfilePhoto();
        acao();
    }

    public void getProfilePhoto(){
        StorageReference reference = storage.getReference("upload/images/" + Session.user.getIdUSer() + ".jpg");

        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            reference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if(taskSnapshot != null){
                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                        profile.setImageBitmap(bitmap);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepararActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Perfil do Usuário");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(TelaPerfilUsuario.this, TelaConversa.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
                    finish();
                }else{
                    Toast.makeText(TelaPerfilUsuario.this, "Erro ao deslogar, tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}