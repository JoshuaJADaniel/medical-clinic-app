package com.example.medical_clinic_app.appointment;

import java.io.Serializable;

public interface Appointment extends Serializable {
    long getDate();
    void setDate(long date);

    String getDoctor();
    void setDoctor(String doctor);

    String getPatient();
    void setPatient(String patient);
}
