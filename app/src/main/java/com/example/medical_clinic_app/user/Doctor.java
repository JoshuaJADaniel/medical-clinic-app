package com.example.medical_clinic_app.user;

import java.util.List;

public interface Doctor {
    List<Long> getAvailabilities();
    List<Integer> getAppointments();
    void addAppointment(int appointmentId);
}
