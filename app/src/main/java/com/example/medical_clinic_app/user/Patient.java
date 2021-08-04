package com.example.medical_clinic_app.user;

import java.util.List;

public interface Patient extends User {
    long getDateOfBirth();
    void setDateOfBirth(long dateOfBirth);

    List<Integer> getAppointments();
    void setAppointments(List<Integer> appointments);
}
