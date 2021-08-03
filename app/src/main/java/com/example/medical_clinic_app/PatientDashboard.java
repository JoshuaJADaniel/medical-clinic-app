package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.medical_clinic_app.user.Patient;

public class PatientDashboard extends AppCompatActivity {
    public static final String KEY_USER = "key_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        Intent intent = getIntent();
        Patient patient = (Patient) intent.getSerializableExtra(KEY_USER);

        Log.i(null, "Dashboard received patient: " + patient);
    }
}