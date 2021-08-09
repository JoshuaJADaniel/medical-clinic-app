package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.medical_clinic_app.adapters.AdapterRecyclerAppointments;
import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.utils.CommonToasts;
import com.example.medical_clinic_app.utils.FormatDoctorsAppointment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorDashboardActivity extends AppCompatActivity {
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private String doctorUsername;
    private final String emptyRecyclerMessage = "You have no appointments, book on below!";
    private final String filledRecyclerMessage = "Click on any row below to view more details";

    private final List<Appointment> pastAppointments = new ArrayList<>();
    private final List<Appointment> upcomingAppointments = new ArrayList<>();
    private final List<Appointment> visibleAppointments = new ArrayList<>();

    private TextView txtRecyclerMessage;
    private RecyclerView recyclerAppointments;
    private AdapterRecyclerAppointments adapterRecyclerAppointments;

    private ToggleButton btnTogglePast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        Intent intent = getIntent();
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);

        btnTogglePast = findViewById(R.id.btnTogglePast);
        recyclerAppointments = findViewById(R.id.recyclerAppointments);
        txtRecyclerMessage = findViewById(R.id.txtRecyclerMessage);
        txtRecyclerMessage.setText(emptyRecyclerMessage);

        setAppointmentsAdapter();
        populateDashboard();
    }

    private void populateDashboard() {
        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();

        dao.getDoctor(doctorUsername, doctor -> {
            if (doctor == null) {
                CommonToasts.databaseDoctorError(DoctorDashboardActivity.this);
                return;
            }

            if (doctor.getAppointments() == null) {
                return;
            }

            for (String id : doctor.getAppointments()) {
                dao.getAppointment(id, appointment -> {
                    if (appointment == null) {
                        CommonToasts.databaseAppointmentError(DoctorDashboardActivity.this);
                        return;
                    }

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime appointmentDate = dateConverter.longToDate(appointment.getDate());

                    if (appointmentDate.isBefore(now)) {
                        pastAppointments.add(appointment);
                    } else {
                        visibleAppointments.add(appointment);
                        upcomingAppointments.add(appointment);
                        adapterRecyclerAppointments.notifyItemInserted(visibleAppointments.size() - 1);
                        txtRecyclerMessage.setText(filledRecyclerMessage);
                    }
                });
            }
        });
    }

    private void setAppointmentsAdapter() {
        adapterRecyclerAppointments = new AdapterRecyclerAppointments(visibleAppointments, new FormatDoctorsAppointment());
        recyclerAppointments.setAdapter(adapterRecyclerAppointments);
        recyclerAppointments.setItemAnimator(new DefaultItemAnimator());
        recyclerAppointments.setLayoutManager(new LinearLayoutManager(this));
    }

    public void togglePast(View view) {
        visibleAppointments.clear();
        visibleAppointments.addAll(btnTogglePast.isChecked() ? pastAppointments : upcomingAppointments);
        txtRecyclerMessage.setText(visibleAppointments.size() == 0 ? emptyRecyclerMessage : filledRecyclerMessage);
        adapterRecyclerAppointments.notifyDataSetChanged();
    }

    public void transferToSchedule(View view) {
        Toast.makeText(this, "Needs to be implemented soon", Toast.LENGTH_LONG).show();
    }
}

