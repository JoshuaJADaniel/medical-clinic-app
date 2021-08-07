package com.example.medical_clinic_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.medical_clinic_app.adapters.AdapterRecyclerAppointments;
import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.appointment.GeneralAppointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.user.Patient;
import com.example.medical_clinic_app.user.PatientObj;
import com.example.medical_clinic_app.utils.FormatPatientsAppointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatientDashboardActivity extends AppCompatActivity {
    public static final String KEY_PATIENT = "KEY_PATIENT";

    private Patient patient;

    private ToggleButton btnTogglePast;

    private List<Appointment> pastAppointments;
    private List<Appointment> upcomingAppointments;

    private TextView txtPastEmpty;
    private TextView txtUpcomingEmpty;

    private RecyclerView recyclerPastAppointments;
    private RecyclerView recyclerUpcomingAppointments;
    private AdapterRecyclerAppointments adapterRecyclerPastAppointments;
    private AdapterRecyclerAppointments adapterRecyclerUpcomingAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        pastAppointments = new ArrayList<>();
        upcomingAppointments = new ArrayList<>();

        btnTogglePast = findViewById(R.id.btnTogglePast);

        txtPastEmpty = findViewById(R.id.txtPastEmpty);
        txtUpcomingEmpty = findViewById(R.id.txtUpcomingEmpty);

        recyclerPastAppointments = findViewById(R.id.recyclerAppointmentsPast);
        recyclerUpcomingAppointments = findViewById(R.id.recyclerAppointmentsUpcoming);

        setAppointmentsAdapter();
        Intent intent = getIntent();
        retrievePatient(intent.getStringExtra(KEY_PATIENT));
    }

    private void retrievePatient(String username) {
        ClinicDao dao = new ClinicFirebaseDao();
        dao.getPatientsRef().child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patient = snapshot.getValue(PatientObj.class);
                populateAppointments();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(null, error.toString());
            }
        });
    }

    private void populateAppointments() {
        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();
        DatabaseReference appointmentsRef = dao.getAppointmentsRef();
        if (patient.getAppointments() == null) return;

        for (int id : patient.getAppointments()) {
            appointmentsRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Appointment appointment = snapshot.getValue(GeneralAppointment.class);
                    LocalDateTime date = dateConverter.longToDate(Objects.requireNonNull(appointment).getDate());
                    LocalDateTime now = LocalDateTime.now();

                    if (date.isBefore(now)) {
                        pastAppointments.add(appointment);
                        adapterRecyclerPastAppointments.notifyItemInserted(pastAppointments.size() - 1);
                    } else {
                        upcomingAppointments.add(appointment);
                        adapterRecyclerPastAppointments.notifyItemInserted(upcomingAppointments.size() - 1);

                        txtPastEmpty.setVisibility(View.INVISIBLE);
                        recyclerUpcomingAppointments.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(null, error.toString());
                }
            });
        }
    }

    private void setAppointmentsAdapter() {
        adapterRecyclerPastAppointments = new AdapterRecyclerAppointments(pastAppointments, new FormatPatientsAppointment());
        recyclerPastAppointments.setAdapter(adapterRecyclerPastAppointments);
        recyclerPastAppointments.setItemAnimator(new DefaultItemAnimator());
        recyclerPastAppointments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapterRecyclerUpcomingAppointments = new AdapterRecyclerAppointments(upcomingAppointments, new FormatPatientsAppointment());
        recyclerUpcomingAppointments.setAdapter(adapterRecyclerUpcomingAppointments);
        recyclerUpcomingAppointments.setItemAnimator(new DefaultItemAnimator());
        recyclerUpcomingAppointments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void togglePast(View view) {
        if (btnTogglePast.isChecked()) {
            txtUpcomingEmpty.setVisibility(View.INVISIBLE);
            recyclerUpcomingAppointments.setVisibility(View.INVISIBLE);
            if (pastAppointments.size() == 0) {
                txtPastEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerPastAppointments.setVisibility(View.VISIBLE);
            }
        } else {
            txtPastEmpty.setVisibility(View.INVISIBLE);
            recyclerPastAppointments.setVisibility(View.INVISIBLE);
            if (upcomingAppointments.size() == 0) {
                txtUpcomingEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerUpcomingAppointments.setVisibility(View.VISIBLE);
            }
        }
    }

    public void transferToBookNewAppointment(View view) {
        Intent intent = new Intent(this, BookNewAppointmentActivity.class);
        startActivity(intent);
    }

}