package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;

public class PatientLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);

        TextView txtUsername = findViewById(R.id.edtTxtName);
        TextView txtPassword = findViewById(R.id.edtTxtPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            ClinicDao dao = new ClinicFirebaseDao();
            String password = txtPassword.getText().toString();
            String username = txtUsername.getText().toString().trim();

            if (username.length() == 0 || password.length() == 0) {
                Toast.makeText(this, "Enter a valid username and password", Toast.LENGTH_LONG).show();
                return;
            }

            dao.getPatient(username, patient -> {
                if (patient == null || !patient.getPassword().equals(password)) {
                    Toast.makeText(PatientLoginActivity.this, "Patient username or password is incorrect", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientLoginActivity.this, "Successfully logged in as " + username, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PatientLoginActivity.this, PatientDashboardActivity.class);
                    intent.putExtra(PatientDashboardActivity.KEY_PATIENT, username);
                    startActivity(intent);
                }
            });
        });

        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(PatientLoginActivity.this, PatientSignupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}