package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.medical_clinic_app.adapters.AdapterRecyclerAppointments;
import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.utils.CommonToasts;
import com.example.medical_clinic_app.utils.FormatPatientsAppointment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity {
    public static final String KEY_PATIENT = "KEY_PATIENT";

    private String patientUsername;
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
        setContentView(R.layout.activity_patient_dashboard);

        Intent intent = getIntent();
        patientUsername = intent.getStringExtra(KEY_PATIENT);

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

        dao.getPatient(patientUsername, patient -> {
            if (patient == null) {
                CommonToasts.databasePatientError(PatientDashboardActivity.this);
                return;
            }

            if (patient.getAppointments() == null) {
                return;
            }

            for (String id : patient.getAppointments()) {
                dao.getAppointment(id, appointment -> {
                    if (appointment == null) {
                        CommonToasts.databaseAppointmentError(PatientDashboardActivity.this);
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
        adapterRecyclerAppointments = new AdapterRecyclerAppointments(visibleAppointments, new FormatPatientsAppointment());
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

    public void transferToBookAppointments(View view) {
        Intent intent = new Intent(this, AvailableDoctorListActivity.class);
        intent.putExtra(AvailableDoctorListActivity.KEY_PATIENT, patientUsername);
        startActivity(intent);
    }

    public void transferToPastDoctors(View view) {
        Intent intent = new Intent(this, PatientPastDoctorsActivity.class);
        intent.putExtra(PatientPastDoctorsActivity.KEY_PATIENT, patientUsername);
        startActivity(intent);
    }
}