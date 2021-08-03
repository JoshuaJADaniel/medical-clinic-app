package com.example.medical_clinic_app.appointment;

public interface Appointment {
    long getDate();
    void setDate(long date);

    String getDoctor();
    void setDoctor(String doctor);

    String getPatient();
    void setPatient(String patient);
}
