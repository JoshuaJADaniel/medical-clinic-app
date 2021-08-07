package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.medical_clinic_app.utils.ErrorToasts;

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
        ClinicDao dao = new ClinicFirebaseDao();
        String name = edtTxtName.getText().toString().trim();
        String password = edtTxtPassword.getText().toString();
        String username = edtTxtUsername.getText().toString().trim();

        if (name.length() == 0 || username.length() == 0 || password.length() == 0) {
            ErrorToasts.emptyFieldsError(this);
            return;
        }

        try {
            dao.validateUsername(username);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        List<String> appointments = new ArrayList<>();
        String gender = spnGenders.getSelectedItem().toString();
        String specialization = spnSpecializations.getSelectedItem().toString();
        Doctor enteredDoctor = new DoctorObj(name, gender, username, password, specialization, appointments, true);

        dao.getDoctor(username, doctor -> {
            if (doctor == null) {
                dao.getDoctorsRef().child(username).setValue(enteredDoctor);
                Toast.makeText(view.getContext(), "Successfully created your account: " + username, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), DoctorDashboardActivity.class);
                intent.putExtra(DoctorDashboardActivity.KEY_DOCTOR, username);
                startActivity(intent);
            } else {
                ErrorToasts.usernameTaken(DoctorSignupActivity.this);
            }
        });
    }

    public void loginAction(View view) {
        Intent intent = new Intent(view.getContext(), DoctorLoginActivity.class);
        startActivity(intent);
    }
}