package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClinicFirebaseDao clinic = new ClinicFirebaseDao();

        setContentView(R.layout.activity_main);
    }

    public void bookAppointment(View view) {
        Intent intent = new Intent(this, DisplayDoctors.class);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("doctors");


        startActivity(intent);
    }
}