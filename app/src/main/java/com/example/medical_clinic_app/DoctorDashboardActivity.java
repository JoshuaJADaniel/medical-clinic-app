package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.medical_clinic_app.adapters.AdapterRecyclerAppointments;
import com.example.medical_clinic_app.appointment.Appointment;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.utils.ErrorToasts;
import com.example.medical_clinic_app.utils.FormatDoctorsAppointment;

import java.util.ArrayList;

public class DoctorDashboardActivity extends AppCompatActivity {
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private String doctorUsername;
    private List<Appointment> upcomingAppointments;

    private RecyclerView recyclerUpcomingAppointments;
    private AdapterRecyclerAppointments adapterRecyclerUpcomingAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        upcomingAppointments = new ArrayList<>();

        recyclerUpcomingAppointments = findViewById(R.id.recyclerDoctorAppointments);

        Intent intent = getIntent();
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);

        if (doctorUsername == null) {
            ErrorToasts.databaseDoctorError(this);
        } else {
            setAppointmentsAdapter();
            populateDashBoard();
        }


    }

    private void populateDashBoard() {
        ClinicDao dao = new ClinicFirebaseDao();

        dao.getDoctor(doctorUsername, doctor -> {
            if (doctor.getAppointments() == null) {
                ErrorToasts.databasePatientError(DoctorDashboardActivity.this);
                return;
            }

            for (String id : doctor.getAppointments()) {
                dao.getAppointment(id, appointment -> {
                    if (appointment == null) {
                        ErrorToasts.databaseAppointmentError(DoctorDashboardActivity.this);
                        return;
                    }

                    upcomingAppointments.add(appointment);
                    adapterRecyclerUpcomingAppointments.notifyItemInserted(upcomingAppointments.size() - 1);

                });
            }
        });

    }

    private void setAppointmentsAdapter() {
        adapterRecyclerUpcomingAppointments = new AdapterRecyclerAppointments(upcomingAppointments, new FormatDoctorsAppointment(), true);
        recyclerUpcomingAppointments.setAdapter(adapterRecyclerUpcomingAppointments);
        recyclerUpcomingAppointments.setItemAnimator(new DefaultItemAnimator());
        recyclerUpcomingAppointments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}

