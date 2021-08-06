package com.example.medical_clinic_app.utils;

import android.widget.TextView;

import com.example.medical_clinic_app.appointment.Appointment;

public interface FormatUsersAppointmentStrategy {
    void formatTxtUser(TextView txtUser, Appointment appointment);
}
