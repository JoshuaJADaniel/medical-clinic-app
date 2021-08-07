package com.example.medical_clinic_app.utils;

import android.widget.TextView;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;

public class FormatDoctorsAppointment implements FormatUsersAppointmentStrategy {
    @Override
    public void formatTxtUser(TextView txtUser, Appointment appointment) {
        ClinicDao dao = new ClinicFirebaseDao();
        dao.getPatient(appointment.getPatient(), patient -> {
            txtUser.setText(patient == null ? "Error" : patient.getName());
        });
    }
}
