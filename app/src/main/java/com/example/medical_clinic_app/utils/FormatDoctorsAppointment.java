package com.example.medical_clinic_app.utils;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.user.Patient;
import com.example.medical_clinic_app.user.PatientObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FormatDoctorsAppointment implements FormatUsersAppointmentStrategy {
    @Override
    public void formatTxtUser(TextView txtUser, Appointment appointment) {
        ClinicDao dao = new ClinicFirebaseDao();
        dao.getPatientsRef().child(appointment.getPatient()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Patient patient = snapshot.getValue(PatientObj.class);
                txtUser.setText(Objects.requireNonNull(patient).getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(null, error.toString());
            }
        });
    }
}
