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
import com.example.medical_clinic_app.utils.CommonToasts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientSignupActivity extends AppCompatActivity {
    private Toolbar toolbar;

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

        toolbar = findViewById(R.id.toolbarPatientSignup);

        edtTxtName = findViewById(R.id.edtTxtName);
        edtTxtUsername = findViewById(R.id.edtTxtUsername);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);

        edtNumDay = findViewById(R.id.edtNumDay);
        edtNumMonth = findViewById(R.id.edtNumMonth);
        edtNumYear = findViewById(R.id.edtNumYear);

        spnGenders = findViewById(R.id.spnGenders);

        initializeToolbar();
        initializeSpinners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);
    }

    private void initializeSpinners() {
        List<String> genders = new ArrayList<>(UserGenders.getGenders());
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        gendersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenders.setAdapter(gendersAdapter);
    }

    public void signupAction(View view) {
        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();

        String name = edtTxtName.getText().toString().trim();
        String password = edtTxtPassword.getText().toString();
        String username = edtTxtUsername.getText().toString().trim();
        String gender = spnGenders.getSelectedItem().toString();

        String strDayNum = edtNumDay.getText().toString();
        String strMonthNum = edtNumMonth.getText().toString();
        String strYearNum = edtNumYear.getText().toString();

        if (name.length() == 0 || username.length() == 0 || password.length() == 0 ||
                strDayNum.length() == 0 || strMonthNum.length() == 0 || strYearNum.length() == 0) {
            CommonToasts.emptyFieldsError(this);
            return;
        }

        try {
            dao.validateUsername(username);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        int dayOfBirth = Integer.parseInt(strDayNum);
        int monthOfBirth = Integer.parseInt(strMonthNum);
        int yearOfBirth = Integer.parseInt(strYearNum);
        long dateOfBirth;

        try {
            int minimumYearOfBirth = 1800;
            LocalDateTime date = LocalDateTime.of(yearOfBirth, monthOfBirth, dayOfBirth, 0, 0);
            if (yearOfBirth < minimumYearOfBirth || date.isAfter(LocalDateTime.now())) throw new Exception();
            dateOfBirth = dateConverter.dateToLong(date);
        } catch (Exception e) {
            CommonToasts.invalidDateEntered(this);
            return;
        }

        Patient newPatient = new PatientObj(name, gender, username, password, dateOfBirth, new ArrayList<>());

        dao.getPatient(username, dbPatient -> {
            if (dbPatient == null) {
                dao.getPatientsRef().child(username).setValue(newPatient);
                CommonToasts.signupSuccess(PatientSignupActivity.this, username);
                Intent intent = new Intent(PatientSignupActivity.this, PatientDashboardActivity.class);
                intent.putExtra(PatientDashboardActivity.KEY_PATIENT, username);
                startActivity(intent);
            } else {
                CommonToasts.usernameTaken(PatientSignupActivity.this);
            }
        });
    }

    public void loginAction(View view) {
        Intent intent = new Intent(PatientSignupActivity.this, PatientLoginActivity.class);
        startActivity(intent);
    }
}