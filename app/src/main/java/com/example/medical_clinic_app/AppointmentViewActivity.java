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
import com.example.medical_clinic_app.utils.CommonToasts;

public class AppointmentViewActivity extends AppCompatActivity {
    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_PATIENT = "KEY_PATIENT";
    public static final String KEY_IS_DOCTOR = "KEY_IS_DOCTOR";
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private Toolbar toolbar;

    private String doctorUsername;
    private String patientUsername;
    private long appointmentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_view);

        toolbar = findViewById(R.id.toolbarAppointment);

        Intent intent = getIntent();
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);
        patientUsername = intent.getStringExtra(KEY_PATIENT);
        appointmentTime = intent.getLongExtra(KEY_TIME, 0);

        if (!intent.getBooleanExtra(KEY_IS_DOCTOR, false)) {
            Button btnPastDoctors = findViewById(R.id.btnPastDoctors);
            btnPastDoctors.setVisibility(View.GONE);
        }

        initializeToolbar();
        populatePatient();
        populateDoctor();
        populateTime();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);
    }

    public void viewPastDoctors(View view) {
        Intent nextIntent = new Intent(AppointmentViewActivity.this, PatientPastDoctorsActivity.class);
        nextIntent.putExtra(PatientPastDoctorsActivity.KEY_PATIENT, patientUsername);
        startActivity(nextIntent);
    }

    private void populatePatient() {
        ClinicDao dao = new ClinicFirebaseDao();
        TextView txtPatientName = findViewById(R.id.txtPatientName);
        TextView txtPatientGender = findViewById(R.id.txtPatientGender);
        TextView txtPatientDob = findViewById(R.id.txtPatientDob);
        DateConverter dateConverter = dao.defaultDateConverter();

        dao.getPatient(patientUsername, patient -> {
            if (patient == null) {
                CommonToasts.databasePatientError(AppointmentViewActivity.this);
            } else {
                txtPatientName.setText(patient.getName());
                txtPatientGender.setText(patient.getGender());
                txtPatientDob.setText(dateConverter.getFormattedDate(patient.getDateOfBirth()));
            }
        });
    }

    private void populateDoctor() {
        ClinicDao dao = new ClinicFirebaseDao();
        TextView txtDoctorName = findViewById(R.id.txtDoctorName);
        TextView txtDoctorGender = findViewById(R.id.txtDoctorGender);
        TextView txtDoctorSpecialization = findViewById(R.id.txtDoctorSpecialization);
        TextView txtDoctorStatus = findViewById(R.id.txtDoctorStatus);

        dao.getDoctor(doctorUsername, doctor -> {
            if (doctor == null) {
                CommonToasts.databaseDoctorError(AppointmentViewActivity.this);
            } else {
                txtDoctorName.setText(doctor.getName());
                txtDoctorGender.setText(doctor.getGender());
                txtDoctorSpecialization.setText(doctor.getSpecialization());
                txtDoctorStatus.setText(doctor.getIsActive() ? "Active" : "Inactive");
            }
        });
    }

    private void populateTime() {
        ClinicDao dao = new ClinicFirebaseDao();
        TextView txtDate = findViewById(R.id.txtDate);
        DateConverter dateConverter = dao.defaultDateConverter();
        txtDate.setText(String.format("%s at %s %s",
                dateConverter.getFormattedDate(appointmentTime),
                dateConverter.getFormattedTime(appointmentTime),
                dateConverter.getLocale()
        ));
    }
}