package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.medical_clinic_app.user.Patient;

public class BookNewAppointment extends AppCompatActivity {
    public static final String KEY_PATIENT = "key_patient";

    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_appointment);

        // Intent values
        Intent intent = getIntent();
        patient = (Patient)intent.getSerializableExtra(KEY_PATIENT);
        Log.d(null, "Booking appointments for: " + patient);
    }
}