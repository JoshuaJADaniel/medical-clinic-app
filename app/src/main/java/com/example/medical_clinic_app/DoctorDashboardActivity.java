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
import android.view.View;
import android.widget.Button;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.utils.CommonToasts;
import com.example.medical_clinic_app.utils.FormatDoctorsAppointment;

import java.util.ArrayList;

public class DoctorDashboardActivity extends AppCompatActivity {
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private String doctorUsername;
    private List<Appointment> upcomingAppointments;

    private RecyclerView recyclerUpcomingAppointments;
    private AdapterRecyclerAppointments adapterRecyclerUpcomingAppointments;

    private Button btnViewSchedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);




        upcomingAppointments = new ArrayList<>();

        recyclerUpcomingAppointments = findViewById(R.id.recyclerDoctorAppointments);

        Intent intent = getIntent();
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);

        if (doctorUsername == null) {
            CommonToasts.databaseDoctorError(this);
        } else {
            setAppointmentsAdapter();
            populateDashBoard();
        }

        btnViewSchedule = findViewById(R.id.btnViewSchedule);
        btnViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorDashboardActivity.this, DoctorTimeSlotActivity.class);
                intent.putExtra(KEY_DOCTOR, doctorUsername);
                startActivity(intent);
            }
        });


    }

    private void populateDashBoard() {
        ClinicDao dao = new ClinicFirebaseDao();

        dao.getDoctor(doctorUsername, doctor -> {
            if (doctor.getAppointments() == null) {
                CommonToasts.databasePatientError(DoctorDashboardActivity.this);
                return;
            }

            for (String id : doctor.getAppointments()) {
                dao.getAppointment(id, appointment -> {
                    if (appointment == null) {
                        CommonToasts.databaseAppointmentError(DoctorDashboardActivity.this);
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

