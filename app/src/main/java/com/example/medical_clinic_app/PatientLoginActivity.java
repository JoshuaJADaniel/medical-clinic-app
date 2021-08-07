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
import com.example.medical_clinic_app.utils.ErrorToasts;

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
                ErrorToasts.emptyFieldsError(PatientLoginActivity.this);
                return;
            }

            dao.getPatient(username, patient -> {
                if (patient == null || !patient.getPassword().equals(password)) {
                    ErrorToasts.usernamePasswordError(PatientLoginActivity.this);
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