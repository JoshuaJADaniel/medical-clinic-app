package com.example.medical_clinic_app;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medical_clinic_app.adapters.AdapterRecyclerPreviouslySeenDoctors;
import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.appointment.GeneralAppointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.utils.ErrorToasts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.medical_clinic_app.adapters.AdapterRecyclerDoctorsAvailable;
import com.example.medical_clinic_app.adapters.AdapterRecyclerPreviouslySeenDoctors;

import com.example.medical_clinic_app.user.Doctor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.example.medical_clinic_app.user.DoctorObj;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.user.DoctorSpecializations;
import com.example.medical_clinic_app.user.UserGenders;
import com.example.medical_clinic_app.utils.ErrorToasts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class AppointmentViewActivityDoctor extends AppCompatActivity{

    private List<Doctor> doctorList;

    private RecyclerView recyclerDoctorList;
    private AdapterRecyclerPreviouslySeenDoctors adapterDoctorList;







    public static final String KEY_PATIENT = "KEY_PATIENT";
    public static final String KEY_DOCTOR = "KEY_DOCTOR";
    public static final String KEY_TIME = "KEY_TIME";

    private String patientUsername = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_view_doctor);

        //Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbarDoctorAppointment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);


        //set up Patient information
        Intent intent = getIntent();
        patientUsername = intent.getStringExtra(KEY_PATIENT);
        TextView txtName = findViewById(R.id.txtDisplayName);
        TextView txtGender = findViewById(R.id.txtDisplayGender);
        TextView txtDOB = findViewById(R.id.txtDisplayDOB);
        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();
        dao.getPatient(patientUsername, patient -> {
            if(patient == null){
                ErrorToasts.databaseDoctorError(AppointmentViewActivityDoctor.this);
            }else{
                txtName.setText(patient.getName());
                txtGender.setText(patient.getGender());
                txtDOB.setText(String.format("%s", dateConverter.getFormattedDate(patient.getDateOfBirth())));

            }
        });



        //Create adapter for previously seen doctor recyclerView
        recyclerDoctorList = findViewById(R.id.recyclerpreviousdoctors);
        doctorList =  new ArrayList<>();
        //need to change to another adapter, no clickable.
        setDoctorAdapter();
        setDoctorList();



    }

    private void setDoctorList() {
        new ClinicFirebaseDao().getAppointmentsRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {

                    Appointment appointment = datasnapshot.getValue(GeneralAppointment.class);
                    if (appointment.getDate() < System.currentTimeMillis()) {

                        if (appointment == null) {
                            ErrorToasts.databaseDoctorError(AppointmentViewActivityDoctor.this);
                            return;
                        } else {
                            String docName = appointment.getDoctor();
                            new ClinicFirebaseDao().getDoctor(docName, doc -> {
                                //my way to avoid duplicates. kinda dumb, but .contains didn't work
                                //and I couldn't come up with sth else
                                if (doctorList == null) {
                                    doctorList.add(doc);
                                    adapterDoctorList.notifyItemInserted(doctorList.size() - 1);
                                } else {
                                    int counter = 0;
                                    for (Doctor doctor : doctorList) {
                                        if (!(doctor.getName().equals(doc.getName()))) counter++;
                                    }
                                    if (counter == doctorList.size()) {
                                        doctorList.add(doc);
                                        adapterDoctorList.notifyItemInserted(doctorList.size() - 1);
                                    }
                                }


                            });
                        }


                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(null, error.toString());
                ErrorToasts.databaseDoctorError(AppointmentViewActivityDoctor.this);
            }
        });

    }

    private void setDoctorAdapter() {
        adapterDoctorList = new AdapterRecyclerPreviouslySeenDoctors(doctorList, patientUsername);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerDoctorList.setItemAnimator(new DefaultItemAnimator());
        recyclerDoctorList.setLayoutManager(layoutManager);
        recyclerDoctorList.setAdapter(adapterDoctorList);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
