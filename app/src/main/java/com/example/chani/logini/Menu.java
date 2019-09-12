package com.example.chani.logini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextView lblCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        firebaseAuth = FirebaseAuth.getInstance();
        lblCorreo = findViewById(R.id.lblCorreo);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseAuthCurrentUser = firebaseAuth.getCurrentUser();
        if(firebaseAuthCurrentUser != null){
            lblCorreo.setText(firebaseAuthCurrentUser.getEmail());
        }
    }


    public void irMultimedia(View view) {
        Intent intent = new Intent(getApplicationContext(), Multimedia.class);
        startActivity(intent);
    }

    public void cerrarSesion(View view) {
        firebaseAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    public void irBaseDatos(View view) {

    }
}
