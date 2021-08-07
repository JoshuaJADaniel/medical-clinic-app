package com.example.medical_clinic_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.user.DoctorObj;
import com.example.medical_clinic_app.user.Patient;
import com.example.medical_clinic_app.user.PatientObj;
import com.example.medical_clinic_app.utils.ErrorMessages;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.Objects;

public class AppointmentViewActivity extends AppCompatActivity {
    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_PATIENT = "KEY_PATIENT";
    public static final String KEY_DOCTOR_PERSPECTIVE = "KEY_DOCTOR_PERSPECTIVE";
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_view);

        Intent intent = getIntent();

        populatePatient(intent.getStringExtra(KEY_PATIENT));
        populateDoctor(intent.getStringExtra(KEY_DOCTOR));
        populateTime(intent.getLongExtra(KEY_TIME, 0));

        Button btnPastDoctors = findViewById(R.id.btnPastDoctors);
        if (!intent.getBooleanExtra(KEY_DOCTOR_PERSPECTIVE, false)) {
            btnPastDoctors.setVisibility(View.GONE);
        }
    }

    private void populatePatient(String username) {
        ClinicDao dao = new ClinicFirebaseDao();
        TextView txtPatientName = findViewById(R.id.txtPatientName);
        TextView txtPatientGender = findViewById(R.id.txtPatientGender);
        TextView txtPatientDob = findViewById(R.id.txtPatientDob);
        DateConverter dateConverter = dao.defaultDateConverter();

        dao.getPatient(username, patient -> {
            if (patient == null) {
                ErrorMessages.databaseToast(AppointmentViewActivity.this);
                return;
            }

            txtPatientName.setText(patient.getName());
            txtPatientGender.setText(patient.getGender());
            LocalDateTime dateOfBirth = dateConverter.longToDate(patient.getDateOfBirth());
            txtPatientDob.setText(dateOfBirth.toString());
        });
    }

    private void populateDoctor(String username) {
        ClinicDao dao = new ClinicFirebaseDao();
        TextView txtDoctorName = findViewById(R.id.txtDoctorName);
        TextView txtDoctorGender = findViewById(R.id.txtDoctorGender);
        TextView txtDoctorSpecialization = findViewById(R.id.txtDoctorSpecialization);
        TextView txtDoctorStatus = findViewById(R.id.txtDoctorStatus);


        dao.getDoctor(username, doctor -> {
            if (doctor == null) {
                ErrorMessages.databaseToast(AppointmentViewActivity.this);
                return;
            }

            txtDoctorName.setText(doctor.getName());
            txtDoctorGender.setText(doctor.getGender());
            txtDoctorSpecialization.setText(doctor.getSpecialization());
            txtDoctorStatus.setText(doctor.getIsActive() ? "Active" : "Inactive");
        });
    }

    private void populateTime(long time) {
        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();
        LocalDateTime date = dateConverter.longToDate(time);
        TextView txtDate = findViewById(R.id.txtDate);
        txtDate.setText(date.toString());
    }
}