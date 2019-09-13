package com.example.chani.logini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.chani.logini.Modelos.ModeloActividades;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LecturaEditActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ModeloActividades modeloActividades;
    EditText txtActivity;
    String id;
    Spinner spinnerGrupos, spinnerLecturas;
    Button btnEdit, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_edit);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        txtActivity = findViewById(R.id.txtActividad);
        final List<String> spinnerArrayGrupos = new ArrayList<String>();
        spinnerArrayGrupos.add("TI-701");
        spinnerArrayGrupos.add("GE-501");
        spinnerArrayGrupos.add("IN-101");
        spinnerArrayGrupos.add("ME-301");
        ArrayAdapter<String> adapterGrupos = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArrayGrupos
        );
        adapterGrupos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupos = findViewById(R.id.spinnerGrupo2);
        spinnerGrupos.setAdapter(adapterGrupos);
        final List<String> spinnerArrayLecturas = new ArrayList<String>();
        spinnerArrayLecturas.add("Calculo 1");
        spinnerArrayLecturas.add("Matematicas Ingenieriles");
        spinnerArrayLecturas.add("Calculo Integral");
        spinnerArrayLecturas.add("Español 1");
        spinnerArrayLecturas.add("Inglés");
        ArrayAdapter<String> adapterLecturas = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArrayLecturas
        );
        adapterLecturas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLecturas = findViewById(R.id.spinnerLectura2);
        spinnerLecturas.setAdapter(adapterLecturas);
        if(id.isEmpty()){
            finish();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("lecturas");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModeloActividades a = dataSnapshot.child(id).getValue(ModeloActividades.class);
                if(a!=null){
                    spinnerGrupos.setSelection(spinnerArrayGrupos.indexOf(a.getGroup()));
                    spinnerLecturas.setSelection(spinnerArrayLecturas.indexOf(a.getLecture()));
                    txtActivity.setText(a.getActivity());
                    modeloActividades = a;
                }else{
                    finish();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnEdit = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(id).removeValue();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               modeloActividades.setActivity(txtActivity.getText().toString());
               modeloActividades.setGroup(spinnerGrupos.getSelectedItem().toString());
               modeloActividades.setLecture(spinnerLecturas.getSelectedItem().toString());
               databaseReference.child(id).setValue(modeloActividades);
            }
        });



    }
}
