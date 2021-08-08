package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SelectTimeslotActivity extends AppCompatActivity {
    public static final String KEY_PATIENT = "KEY_PATIENT";
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private String patientUsername;
    private String doctorUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_timeslot);

        Intent intent = getIntent();
        patientUsername = intent.getStringExtra(KEY_PATIENT);
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);

        Toast.makeText(this, String.format("Got %s and %s from intent", patientUsername, doctorUsername), Toast.LENGTH_LONG).show();
    }
}
