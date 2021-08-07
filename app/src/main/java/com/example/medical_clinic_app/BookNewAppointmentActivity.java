package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.medical_clinic_app.adapters.AdapterRecyclerDoctorsAvailable;
import com.example.medical_clinic_app.user.Doctor;

import java.util.ArrayList;
import java.util.List;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.user.DoctorObj;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class BookNewAppointmentActivity extends AppCompatActivity {
    private List<Doctor> DoctorList;
    private Doctor doctor;

    private RecyclerView recyclerDoctorsList;
    private AdapterRecyclerDoctorsAvailable adapterDoctorsList;

    List<Integer> lt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_appointment);

        DoctorList = new ArrayList<>();

        recyclerDoctorsList = findViewById(R.id.recyclerDoctorList);

        setDoctorAdapter();
        setDoctorList();



    }

    public void setDoctorAdapter(){
        adapterDoctorsList = new AdapterRecyclerDoctorsAvailable(DoctorList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDoctorsList.setLayoutManager(layoutManager);
        recyclerDoctorsList.setItemAnimator(new DefaultItemAnimator());
        recyclerDoctorsList.setAdapter(adapterDoctorsList);
    }

    public void setDoctorList(){
        ClinicDao dao = new ClinicFirebaseDao();
        DatabaseReference DoctorDatabase = dao.getDoctorsRef();
        DoctorDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot :snapshot.getChildren()){
                    Doctor doctor = datasnapshot.getValue(DoctorObj.class);
                    DoctorList.add(doctor);
                    //adapterDoctorsList.notifyItemInserted(DoctorList.size()-2);
                }
                adapterDoctorsList.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(null, error.toString());

            }
        });


    }
}