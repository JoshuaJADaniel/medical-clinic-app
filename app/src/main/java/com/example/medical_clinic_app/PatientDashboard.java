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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medical_clinic_app.adapters.AdapterRecyclerAppointments;
import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.appointment.GeneralAppointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.user.Patient;
import com.example.medical_clinic_app.user.PatientObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientDashboard extends AppCompatActivity {
    public static final String KEY_PATIENT = "key_patient";

    private Patient patient;
    private List<Appointment> appointmentList;
    private AdapterRecyclerAppointments adapterRecyclerAppointments;

    private TextView txtEmptyRecycler;
    private RecyclerView recyclerAppointments;
    private LinearLayout linearAppointmentsHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Intent values
        Intent intent = getIntent();
        patient = (Patient)intent.getSerializableExtra(KEY_PATIENT);

        // Page components
        recyclerAppointments = findViewById(R.id.recyclerAppointments);
        linearAppointmentsHeader = findViewById(R.id.linearAppointmentsHeader);
        txtEmptyRecycler = findViewById(R.id.txtEmptyRecycler);

        // Logic
        appointmentList = new ArrayList<>();
        setAppointmentsAdapter();
        populateAppointments();
    }

    public void transferToBookNewAppointments(View view) {
        Intent intent = new Intent(this, BookNewAppointment.class);
        intent.putExtra(BookNewAppointment.KEY_PATIENT, patient);
        startActivity(intent);
    }


    private void setAppointmentsAdapter() {
        adapterRecyclerAppointments = new AdapterRecyclerAppointments(appointmentList);
        recyclerAppointments.setAdapter(adapterRecyclerAppointments);
        recyclerAppointments.setItemAnimator(new DefaultItemAnimator());
        recyclerAppointments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void populateAppointments() {
        ClinicDao dao = new ClinicFirebaseDao();
        DatabaseReference appointmentsRef = dao.getAppointmentsRef();
        if(patient.getAppointments()==null) return; // prevent crash when no appointments
        for (int id : patient.getAppointments()) {
            appointmentsRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Appointment appointment = snapshot.getValue(GeneralAppointment.class);

                    if (appointment == null) return;

                    DateConverter dateConverter = dao.defaultDateConverter();
                    LocalDateTime date = dateConverter.longToDate(appointment.getDate());
                    LocalDateTime now = LocalDateTime.now();

                    if (date.isBefore(now)) {
                        txtEmptyRecycler.setVisibility(View.INVISIBLE);
                        linearAppointmentsHeader.setVisibility(View.VISIBLE);

                        appointmentList.add(appointment);
                        adapterRecyclerAppointments.notifyItemInserted(appointmentList.size() - 1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(null, error.toString());
                }
            });
        }
    }
}