package com.example.medical_clinic_app.user;

import java.util.List;

public interface Doctor extends User {
    List<String> getAppointments();
    void addToAppointments(String id);
    void setAppointments(List<String> appointments);

    String getSpecialization();
    void setSpecialization(String specialization);

    boolean getIsActive();
    void setIsActive(boolean isActive);
}
