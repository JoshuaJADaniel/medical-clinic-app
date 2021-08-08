package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.utils.CommonToasts;

public class DoctorLoginActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TextView txtUsername;
    private TextView txtPassword;

    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbarLogin);

        txtUsername = findViewById(R.id.edtTxtUsername);
        txtPassword = findViewById(R.id.edtTxtPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        initializeToolbar();
        initializeBtnLogin();
        initializeBtnSignup();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Doctor Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);
    }

    private void initializeBtnLogin() {
        btnLogin.setOnClickListener(view -> {
            ClinicDao dao = new ClinicFirebaseDao();
            String password = txtPassword.getText().toString();
            String username = txtUsername.getText().toString().trim();

            if (username.length() == 0 || password.length() == 0) {
                CommonToasts.emptyFieldsError(DoctorLoginActivity.this);
                return;
            }

            dao.getDoctor(username, doctor -> {
                if (doctor == null || !doctor.getPassword().equals(password)) {
                    CommonToasts.usernamePasswordError(DoctorLoginActivity.this);
                } else {
                    CommonToasts.loginSuccess(DoctorLoginActivity.this, username);
                    Intent intent = new Intent(DoctorLoginActivity.this, DoctorDashboardActivity.class);
                    intent.putExtra(DoctorDashboardActivity.KEY_DOCTOR, username);
                    startActivity(intent);
                }
            });
        });
    }

    private void initializeBtnSignup() {
        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(DoctorLoginActivity.this, DoctorSignupActivity.class);
            startActivity(intent);
        });
    }
}
