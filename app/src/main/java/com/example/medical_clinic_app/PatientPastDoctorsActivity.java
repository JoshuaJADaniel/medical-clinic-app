package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.medical_clinic_app.adapters.AdapterRecyclerDoctorsAvailable;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.utils.CommonToasts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PatientPastDoctorsActivity extends AppCompatActivity {
    public static final String KEY_PATIENT = "KEY_PATIENT";

    private String patientUsername;
    private final List<Doctor> pastDoctors = new ArrayList<>();

    private Toolbar toolbar;
    private TextView txtNoPastDoctors;
    private RecyclerView recyclerDoctorList;
    private AdapterRecyclerDoctorsAvailable adapterDoctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_past_doctors);

        patientUsername = getIntent().getStringExtra(KEY_PATIENT);

        toolbar = findViewById(R.id.toolbarPastDoctors);
        txtNoPastDoctors = findViewById(R.id.txtNoPastDoctors);
        recyclerDoctorList = findViewById(R.id.recyclerPastDoctors);
        recyclerDoctorList.setVisibility(View.INVISIBLE);

        initializeToolbar();
        setupDoctorAdapter();
        setupPastDoctors();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);
    }

    private void setupDoctorAdapter() {
        adapterDoctorList = new AdapterRecyclerDoctorsAvailable(pastDoctors);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerDoctorList.setItemAnimator(new DefaultItemAnimator());
        recyclerDoctorList.setLayoutManager(layoutManager);
        recyclerDoctorList.setAdapter(adapterDoctorList);
    }

    private void setupPastDoctors() {
        ClinicDao dao = new ClinicFirebaseDao();
        Set<String> addedDoctors = new HashSet<>();
        DateConverter dateConverter = dao.defaultDateConverter();
        dao.getPatient(patientUsername, patient -> {
            if (patient == null) {
                CommonToasts.databasePatientError(PatientPastDoctorsActivity.this);
                return;
            }

            if (patient.getAppointments() == null) return;

            for (String id : patient.getAppointments()) {
                dao.getAppointment(id, appointment -> {
                    if (appointment == null) {
                        CommonToasts.databaseAppointmentError(PatientPastDoctorsActivity.this);
                    } else if (appointment.getDate() < dateConverter.dateToLong(LocalDateTime.now())) {
                        dao.getDoctor(appointment.getDoctor(), doctor -> {
                            if (doctor == null) {
                                CommonToasts.databaseDoctorError(PatientPastDoctorsActivity.this);
                            } else if (!addedDoctors.contains(doctor.getUsername())) {
                                pastDoctors.add(doctor);
                                adapterDoctorList.notifyItemInserted(pastDoctors.size() - 1);
                                addedDoctors.add(doctor.getUsername());
                                recyclerDoctorList.setVisibility(View.VISIBLE);
                                txtNoPastDoctors.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }
        });
    }
}