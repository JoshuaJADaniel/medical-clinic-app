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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
public class BookNewAppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private List<Doctor> DoctorList;
    private Doctor doctor;

    private RecyclerView recyclerDoctorsList;
    private AdapterRecyclerDoctorsAvailable adapterDoctorsList;
    private String GenderFlag = "Gender";
    public String Gender = "Gender";
    private String Specialization = "Specialization";

    private List<String> GenderList;
    private List<String> SpecializationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_appointment);
        setUpGenderList();
        setUpSpecializationList();


        DoctorList = new ArrayList<>();


        recyclerDoctorsList = findViewById(R.id.recyclerDoctorList);

        setDoctorAdapter();
        // the Spinner will automatically trigger the setDoctorList at the beginning
        //so no need to setDoctorList up
        Spinner GenderSpinner = findViewById(R.id.GenderSpinner);
        ArrayAdapter<CharSequence> GenderAdapter = ArrayAdapter.createFromResource(this,R.array.GenderList, android.R.layout.simple_spinner_item);
        GenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpinner.setAdapter(GenderAdapter);
        GenderSpinner.setOnItemSelectedListener(this);


        Spinner SpecializationSpinner = findViewById(R.id.SpecializationSpinner);
        ArrayAdapter<CharSequence> SpecializationAdapter = ArrayAdapter.createFromResource(this,R.array.SpecializationList, android.R.layout.simple_spinner_item);
        SpecializationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpecializationSpinner.setAdapter(SpecializationAdapter);
        SpecializationSpinner.setOnItemSelectedListener(this);
        setDoctorList(Gender, Specialization);

    }

    private void setUpSpecializationList() {

        SpecializationList = new ArrayList<>();
        SpecializationList.add("Specialization");
        SpecializationList.add("Allergist");
        SpecializationList.add("Cardiologist");
        SpecializationList.add("Dentist");
        SpecializationList.add("Dermatologist");
        SpecializationList.add("Psychiatrist");
        SpecializationList.add("Radiologist");
        SpecializationList.add("Urologist");

    }

    private void setUpGenderList() {
        GenderList = new ArrayList<>();
    }

    public void setDoctorAdapter(){
        adapterDoctorsList = new AdapterRecyclerDoctorsAvailable(DoctorList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDoctorsList.setLayoutManager(layoutManager);
        recyclerDoctorsList.setItemAnimator(new DefaultItemAnimator());
        recyclerDoctorsList.setAdapter(adapterDoctorsList);
    }

    public void setDoctorList(String Gender, String Specialization){
        DoctorList.removeAll(DoctorList);

        ClinicDao dao = new ClinicFirebaseDao();
        DatabaseReference DoctorDatabase = dao.getDoctorsRef();
        DoctorDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot :snapshot.getChildren()) {

                    Doctor doctor = datasnapshot.getValue(DoctorObj.class);
                    if (Gender.equals("Gender") && Specialization.equals("Specialization"))
                        DoctorList.add(doctor);
                    else if (Gender.equals("Gender") && !(Specialization.equals("Specialization"))) {
                        if (doctor.getSpecialization().equals(Specialization))
                            DoctorList.add(doctor);
                    } else if (!(Gender.equals("Gender")) && Specialization.equals("Specialization")) {
                        if (doctor.getGender().equals(Gender)) DoctorList.add(doctor);
                    } else if (doctor.getGender().equals(Gender) && doctor.getSpecialization().equals(Specialization)) DoctorList.add(doctor);
                }

                adapterDoctorsList.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(null, error.toString());

            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString().trim();
        if(SpecializationList.contains(text)){
            Specialization = text;
        }else {
            Gender = text;
        }

        if(!(Gender.equals("Gender")) || !(Specialization.equals("Specialization"))) setDoctorList(Gender, Specialization);

        Toast.makeText(adapterView.getContext(),text, Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}