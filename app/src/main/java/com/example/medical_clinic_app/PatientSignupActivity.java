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
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.user.Patient;
import com.example.medical_clinic_app.user.PatientObj;
import com.example.medical_clinic_app.user.UserGenders;
import com.example.medical_clinic_app.utils.ErrorToasts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientSignupActivity extends AppCompatActivity {
    private EditText edtTxtName;
    private EditText edtTxtUsername;
    private EditText edtTxtPassword;

    private EditText edtNumDay;
    private EditText edtNumMonth;
    private EditText edtNumYear;

    private Spinner spnGenders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);

        Toolbar toolbar = findViewById(R.id.toolbarPatientSignup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);

        // Components
        edtTxtName = findViewById(R.id.edtTxtName);
        edtTxtUsername = findViewById(R.id.edtTxtUsername);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);

        edtNumDay = findViewById(R.id.edtNumDay);
        edtNumMonth = findViewById(R.id.edtNumMonth);
        edtNumYear = findViewById(R.id.edtNumYear);

        spnGenders = findViewById(R.id.spnGenders);

        // Initialize spinners
        ArrayList<String> genders = new ArrayList<>(UserGenders.getGenders());
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
        DateConverter dateConverter = dao.defaultDateConverter();

        String name = edtTxtName.getText().toString().trim();
        String password = edtTxtPassword.getText().toString();
        String username = edtTxtUsername.getText().toString().trim();

        String dayOfBirth = edtNumDay.getText().toString();
        String monthOfBirth = edtNumMonth.getText().toString();
        String yearOfBirth = edtNumYear.getText().toString();

        if (name.length() == 0 || username.length() == 0 || password.length() == 0 ||
                dayOfBirth.length() == 0 || monthOfBirth.length() == 0 || yearOfBirth.length() == 0) {
            ErrorToasts.emptyFieldsError(this);
            return;
        }

        try {
            dao.validateUsername(username);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        int day = Integer.parseInt(dayOfBirth);
        int month = Integer.parseInt(monthOfBirth);
        int year = Integer.parseInt(yearOfBirth);
        long dateOfBirth;

        try {
            int minYear = 1800;
            LocalDateTime date = LocalDateTime.of(year, month, day, 0, 0);
            if (year < minYear || date.isAfter(LocalDateTime.now())) throw new Exception();
            dateOfBirth = dateConverter.dateToLong(date);
        } catch (Exception e) {
            Toast.makeText(this, "Please enter a valid date!", Toast.LENGTH_LONG).show();
            return;
        }

        List<String> appointments = new ArrayList<>();
        String gender = spnGenders.getSelectedItem().toString();
        Patient newPatient = new PatientObj(name, gender, username, password, dateOfBirth, appointments);

        dao.getPatient(username, patient -> {
            if (patient == null) {
                dao.getPatientsRef().child(username).setValue(newPatient);
                Toast.makeText(view.getContext(), "Successfully created your account", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), PatientDashboardActivity.class);
                intent.putExtra(PatientDashboardActivity.KEY_PATIENT, username);
                startActivity(intent);
            } else {
                ErrorToasts.usernameTaken(PatientSignupActivity.this);
            }
        });
    }

    public void loginAction(View view) {
        Intent intent = new Intent(view.getContext(), PatientLoginActivity.class);
        startActivity(intent);
    }
}