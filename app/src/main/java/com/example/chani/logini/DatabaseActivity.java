package com.example.chani.logini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chani.logini.Modelos.ModeloActividades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity {

    Spinner spinnerGrupos, spinnerLecturas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    EditText txtActividad;
    String selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        List<String> spinnerArrayGrupos = new ArrayList<String>();
        spinnerArrayGrupos.add("TI-701");
        spinnerArrayGrupos.add("GE-501");
        spinnerArrayGrupos.add("IN-101");
        spinnerArrayGrupos.add("ME-301");
        ArrayAdapter<String> adapterGrupos = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArrayGrupos
        );
        adapterGrupos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupos = findViewById(R.id.spinnerGrupo);
        spinnerGrupos.setAdapter(adapterGrupos);
        List<String> spinnerArrayLecturas = new ArrayList<String>();
        spinnerArrayLecturas.add("Calculo 1");
        spinnerArrayLecturas.add("Matematicas Ingenieriles");
        spinnerArrayLecturas.add("Calculo Integral");
        spinnerArrayLecturas.add("Español 1");
        spinnerArrayLecturas.add("Inglés");
        ArrayAdapter<String> adapterLecturas = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArrayLecturas
        );
        adapterLecturas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLecturas = findViewById(R.id.spinnerLectura);
        spinnerLecturas.setAdapter(adapterLecturas);
        txtActividad = findViewById(R.id.txtActividad);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("lecturas");
        recyclerView = findViewById(R.id.recycler);
        AdaptadorLecturasFirebase adaptadorFirebase = new AdaptadorLecturasFirebase(ModeloActividades.class,R.layout.modelo_lectura
                ,LecturaHolder.class,databaseReference,DatabaseActivity.this);

        recyclerView.setAdapter(adaptadorFirebase);
        recyclerView.setLayoutManager(new GridLayoutManager(DatabaseActivity.this,1,
                LinearLayoutManager.VERTICAL,false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarLectura();
            }
        });
        selectedID = "";
    }

    private void AgregarLectura() {
        String lectura = spinnerLecturas.getSelectedItem().toString();
        String grupo = spinnerGrupos.getSelectedItem().toString();
        String actividad = txtActividad.getText().toString();
        if(actividad.isEmpty()){
            txtActividad.setError("Falta actividad");
            return;
        }
        String id = databaseReference.push().getKey();
        ModeloActividades modeloActividades = new ModeloActividades(
                id, grupo, lectura, actividad
        );
        databaseReference.child(id).setValue(modeloActividades).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Se ha agregado de manera correcta", Toast.LENGTH_SHORT).show();
                    txtActividad.setText("");
                }
            }
        });

    }
}
