package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.utils.ErrorToasts;

public class AppointmentViewActivity extends AppCompatActivity {
    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_PATIENT = "KEY_PATIENT";
    public static final String KEY_IS_DOCTOR = "KEY_IS_DOCTOR";
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_view);

        Toolbar toolbar = findViewById(R.id.toolbarAppointment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);

        Intent intent = getIntent();
        populatePatient(intent.getStringExtra(KEY_PATIENT));
        populateDoctor(intent.getStringExtra(KEY_DOCTOR));
        populateTime(intent.getLongExtra(KEY_TIME, 0));

        Button btnPastDoctors = findViewById(R.id.btnPastDoctors);
        if (intent.getBooleanExtra(KEY_IS_DOCTOR, false)) {
            btnPastDoctors.setOnClickListener(view -> {
                Intent nextIntent = new Intent(AppointmentViewActivity.this, PatientPastDoctorsActivity.class);
                nextIntent.putExtra(PatientPastDoctorsActivity.KEY_PATIENT, intent.getStringExtra(KEY_PATIENT));
                startActivity(nextIntent);
            });
        } else {
            btnPastDoctors.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void populatePatient(String username) {
        ClinicDao dao = new ClinicFirebaseDao();
        TextView txtPatientName = findViewById(R.id.txtPatientName);
        TextView txtPatientGender = findViewById(R.id.txtPatientGender);
        TextView txtPatientDob = findViewById(R.id.txtPatientDob);
        DateConverter dateConverter = dao.defaultDateConverter();

        dao.getPatient(username, patient -> {
            if (patient == null) {
                ErrorToasts.databasePatientError(AppointmentViewActivity.this);
            } else {
                txtPatientName.setText(patient.getName());
                txtPatientGender.setText(patient.getGender());
                txtPatientDob.setText(dateConverter.getFormattedDate(patient.getDateOfBirth()));
            }
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
                ErrorToasts.databaseDoctorError(AppointmentViewActivity.this);
            } else {
                txtDoctorName.setText(doctor.getName());
                txtDoctorGender.setText(doctor.getGender());
                txtDoctorSpecialization.setText(doctor.getSpecialization());
                txtDoctorStatus.setText(doctor.getIsActive() ? "Active" : "Inactive");
            }
        });
    }

    private void populateTime(long time) {
        ClinicDao dao = new ClinicFirebaseDao();
        TextView txtDate = findViewById(R.id.txtDate);
        DateConverter dateConverter = dao.defaultDateConverter();
        txtDate.setText(String.format("%s at %s %s",
                dateConverter.getFormattedDate(time),
                dateConverter.getFormattedTime(time),
                dateConverter.getLocale()
        ));
    }
}