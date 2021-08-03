package com.example.medical_clinic_app.user;

import java.util.List;

public interface Doctor extends User {
    List<Integer> getAppointments();
    void setAppointments(List<Integer> appointments);
}
