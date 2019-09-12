package com.example.chani.logini;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText txtEmail, txtPass;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtContra);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarGoogle();
            }
        });
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseAuthCurrentUser = firebaseAuth.getCurrentUser();
        if(firebaseAuthCurrentUser != null){
            Intent intent = new Intent(getApplicationContext(), Menu.class);
            startActivity(intent);
        }
    }

    public void registrar(View view) {
        Intent intent = new Intent(getApplicationContext(), Registro.class);
        startActivity(intent);
    }

    public void iniciar(View view) {
        iniciarSesionCorreo();
    }

    public void irMenu(){
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);
    }

    private void iniciarSesionCorreo() {
        String correo, pass;
        correo = txtEmail.getText().toString();
        pass = txtPass.getText().toString();
        if(pass.isEmpty()){
            txtPass.setError("Falta contraseña");
            return;
        }
        if(correo.isEmpty()){
            txtEmail.setError("Falta correo electronico");
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(correo,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            irMenu();
                        }else{
                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void iniciarGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult();
            googleAuthFirebase(account);
        }
    }

    private void googleAuthFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            irMenu();
                        }else{
                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
