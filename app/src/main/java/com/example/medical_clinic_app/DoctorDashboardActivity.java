package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DoctorDashboardActivity extends AppCompatActivity {
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
    }
}