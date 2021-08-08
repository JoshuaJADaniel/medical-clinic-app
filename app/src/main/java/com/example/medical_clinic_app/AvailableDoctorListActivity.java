package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.medical_clinic_app.adapters.AdapterRecyclerDoctorsAvailable;
import com.example.medical_clinic_app.user.Doctor;

import java.util.ArrayList;
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

public class AvailableDoctorListActivity extends AppCompatActivity {
    private List<Doctor> doctorList;
    private List<Doctor> filteredDoctorList;

    private RecyclerView recyclerDoctorList;
    private AdapterRecyclerDoctorsAvailable adapterDoctorList;

    private String currentGender;
    private String currentSpecialization;
    private final String genderPrompt = "Select Gender";
    private final String specializationPrompt = "Select Specialization";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_doctor_list);

        recyclerDoctorList = findViewById(R.id.recyclerDoctorList);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarAvailableDoctors);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);

        // Create adapter
        doctorList = new ArrayList<>();
        filteredDoctorList = new ArrayList<>();
        setDoctorAdapter();
        setDoctorList();

        // Initialize data for spinners
        List<String> genders = new ArrayList<>(UserGenders.getGenders());
        genders.add(0, genderPrompt);

        List<String> specializations = new ArrayList<>(DoctorSpecializations.getSpecializations());
        specializations.add(0, specializationPrompt);

        // Initialize spinners
        Spinner spnGenders = findViewById(R.id.spnGenders);
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        gendersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenders.setAdapter(gendersAdapter);

        Spinner spnSpecializations = findViewById(R.id.spnSpecializations);
        ArrayAdapter<String> specializationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, specializations);
        specializationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSpecializations.setAdapter(specializationsAdapter);

        // Add event listeners to spinners
        spnGenders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                currentGender = adapterView.getItemAtPosition(position).toString();
                filterDoctorList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spnSpecializations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                currentSpecialization = adapterView.getItemAtPosition(position).toString();
                filterDoctorList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setDoctorAdapter() {
        adapterDoctorList = new AdapterRecyclerDoctorsAvailable(filteredDoctorList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerDoctorList.setItemAnimator(new DefaultItemAnimator());
        recyclerDoctorList.setLayoutManager(layoutManager);
        recyclerDoctorList.setAdapter(adapterDoctorList);
    }

    public void setDoctorList() {
        new ClinicFirebaseDao().getDoctorsRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Doctor doctor = datasnapshot.getValue(DoctorObj.class);

                    if (doctor == null) {
                        ErrorToasts.databaseDoctorError(AvailableDoctorListActivity.this);
                        return;
                    }

                    if (doctor.getIsActive()) {
                        doctorList.add(doctor);
                        filteredDoctorList.add(doctor);
                        adapterDoctorList.notifyItemInserted(filteredDoctorList.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(null, error.toString());
                ErrorToasts.databaseDoctorError(AvailableDoctorListActivity.this);
            }
        });

    }

    public void filterDoctorList() {
        if (currentGender == null || currentSpecialization == null) return;

        filteredDoctorList.clear();
        for (Doctor doctor : doctorList) {
            if ((currentGender.equals(genderPrompt) || currentGender.equals(doctor.getGender())) &&
                    (currentSpecialization.equals(specializationPrompt) || currentSpecialization.equals(doctor.getSpecialization()))) {
                filteredDoctorList.add(doctor);
            }
        }

        adapterDoctorList.notifyDataSetChanged();
    }
}