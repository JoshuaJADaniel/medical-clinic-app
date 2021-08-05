package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PatientDashboardActivity extends AppCompatActivity {
    public static final String KEY_PATIENT = "KEY_PATIENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
    }
}