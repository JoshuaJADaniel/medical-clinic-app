package com.example.medical_clinic_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.user.DoctorObj;
import com.example.medical_clinic_app.user.DoctorSpecializations;
import com.example.medical_clinic_app.user.UserGenders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorSignupActivity extends AppCompatActivity {
    private EditText edtTxtName;
    private EditText edtTxtUsername;
    private EditText edtTxtPassword;

    private Spinner spnGenders;
    private Spinner spnSpecializations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);

        Toolbar toolbar = findViewById(R.id.toolbarDoctorSignup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);

        // Components
        edtTxtName = findViewById(R.id.edtTxtName);
        edtTxtUsername = findViewById(R.id.edtTxtUsername);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        spnSpecializations = findViewById(R.id.spnSpecializations);
        spnGenders = findViewById(R.id.spnGenders);

        // Initialize spinner contents
        ArrayList<String> genders = new ArrayList<>(UserGenders.getGenders());
        ArrayList<String> specializations = new ArrayList<>(DoctorSpecializations.getSpecializations());

        // Initialize spinners
        ArrayAdapter<String> specializationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, specializations);
        specializationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSpecializations.setAdapter(specializationsAdapter);

        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        gendersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenders.setAdapter(gendersAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void signupAction(View view) {
        String name = edtTxtName.getText().toString().trim();
        String password = edtTxtPassword.getText().toString();
        String username = edtTxtUsername.getText().toString().trim().toLowerCase();

        if (name.length() == 0 || username.length() == 0 || password.length() == 0) {
            Toast.makeText(this, "Please fill all empty fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!username.matches("[a-z0-9]+")) {
            Toast.makeText(this, "Username must be alphanumeric", Toast.LENGTH_LONG).show();
            return;
        }

        List<String> appointments = new ArrayList<>();
        String gender = spnGenders.getSelectedItem().toString();
        String specialization = spnSpecializations.getSelectedItem().toString();
        Doctor doctor = new DoctorObj(name, gender, username, password, specialization, appointments, true);

        ClinicDao dao = new ClinicFirebaseDao();
        DatabaseReference doctorsRef = dao.getDoctorsRef();
        doctorsRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(DoctorObj.class) == null) {
                    doctorsRef.child(username).setValue(doctor);
                    Toast.makeText(view.getContext(), "Successfully created your account", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(view.getContext(), DoctorDashboardActivity.class);
                    intent.putExtra(DoctorDashboardActivity.KEY_DOCTOR, username);
                    startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "Sorry, that username is already taken", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(null, error.toString());
            }
        });
    }

    public void loginAction(View view) {
        Intent intent = new Intent(view.getContext(), DoctorLoginActivity.class);
        startActivity(intent);
    }
}