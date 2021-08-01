package com.example.medical_clinic_app.user;

import java.util.List;

public interface Patient extends User {
    int getAge();
    List<Integer> getAppointments();
    void addAppointment(int appointmentId);
}
