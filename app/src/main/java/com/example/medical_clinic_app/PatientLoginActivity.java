package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.utils.CommonToasts;

public class PatientLoginActivity extends AppCompatActivity implements LoginContract.View {
    private LoginContract.Presenter loginPresenter;

    private Toolbar toolbar;

    private TextView txtUsername;
    private TextView txtPassword;

    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPresenter = new PatientLoginPresenter(this, new ClinicFirebaseDao());

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
        getSupportActionBar().setTitle("Patient Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);
    }

    private void initializeBtnLogin() {
        btnLogin.setOnClickListener(view -> {
            loginPresenter.validateLogin();
        });
    }

    private void initializeBtnSignup() {
        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(PatientLoginActivity.this, PatientSignupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void loginFailed() {
        CommonToasts.usernamePasswordError(this);
    }

    @Override
    public void loginSuccess(String username) {
        CommonToasts.loginSuccess(this, username);
        Intent intent = new Intent(this, PatientDashboardActivity.class);
        intent.putExtra(PatientDashboardActivity.KEY_PATIENT, username);
        startActivity(intent);
    }

    @Override
    public void emptyFieldsError() {
        CommonToasts.emptyFieldsError(this);
    }

    @Override
    public String getUsername() {
        return txtUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return txtPassword.getText().toString();
    }
}