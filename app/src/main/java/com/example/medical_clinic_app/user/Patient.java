package com.example.medical_clinic_app.user;

import com.example.medical_clinic_app.appointment.Appointment;

import java.util.List;

public interface Patient extends User {
    int getAge();
    List<Appointment> getAppointments();
}
