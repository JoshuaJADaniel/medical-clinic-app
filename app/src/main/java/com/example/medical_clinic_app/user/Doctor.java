package com.example.medical_clinic_app.user;

import com.example.medical_clinic_app.appointment.Appointment;

import java.util.List;

public interface Doctor {
    List<Long> getAvailabilities();
    List<Appointment> getAppointments();
    boolean bookAppointment(String patient, long time);
}
