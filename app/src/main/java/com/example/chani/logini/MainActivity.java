package com.example.chani.logini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText txtEmail, txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtContra);
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
}
