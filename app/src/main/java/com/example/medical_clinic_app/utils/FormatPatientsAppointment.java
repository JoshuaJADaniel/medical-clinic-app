package com.example.medical_clinic_app.utils;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.user.DoctorObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FormatPatientsAppointment implements FormatUsersAppointmentStrategy {
    @Override
    public void formatTxtUser(TextView txtUser, Appointment appointment) {
        ClinicDao dao = new ClinicFirebaseDao();
        dao.getDoctorsRef().child(appointment.getDoctor()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor = snapshot.getValue(DoctorObj.class);
                txtUser.setText(String.format("Dr. %s", Objects.requireNonNull(doctor).getName()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(null, error.toString());
            }
        });
    }
}
