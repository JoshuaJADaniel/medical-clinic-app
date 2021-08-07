package com.example.medical_clinic_app.utils;

import android.widget.TextView;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;

public class FormatPatientsAppointment implements FormatUsersAppointmentStrategy {
    @Override
    public void formatTxtUser(TextView txtUser, Appointment appointment) {
        ClinicDao dao = new ClinicFirebaseDao();
        dao.getDoctor(appointment.getDoctor(), doctor -> {
            txtUser.setText(doctor == null ? "Error" : String.format("Dr. %s", doctor.getName()));
        });
    }
}
