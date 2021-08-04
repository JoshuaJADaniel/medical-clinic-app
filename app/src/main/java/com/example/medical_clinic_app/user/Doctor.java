package com.example.medical_clinic_app.user;

import java.util.List;

public interface Doctor extends User {
    List<Integer> getAppointments();
    void setAppointments(List<Integer> appointments);

    String getSpecialization();
    void setSpecialization(String specialization);

    boolean getIsActive();
    void setIsActive(boolean isActive);
}
