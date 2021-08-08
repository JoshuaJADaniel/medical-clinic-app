package com.example.medical_clinic_app;



import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorAvailableTimeSlotActivity extends AppCompatActivity {

    public static final String KEY_DOCTOR = "KEY_DOCTOR";
    public static final String KEY_PATIENT = "KEY_PATIENT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        TextView docName = findViewById(R.id.txtDocName);
        TextView patName = findViewById(R.id.txtPatName);
        docName.setText(KEY_DOCTOR);
        patName.setText(KEY_PATIENT);
        */

        setContentView(R.layout.activity_doctor_time_slot);
    }

}
