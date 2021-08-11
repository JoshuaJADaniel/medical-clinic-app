package com.example.medical_clinic_app.user;

import java.util.Arrays;
import java.util.List;

public interface Doctor extends User {
    // Based on 24hr clock
    int MIN_WORK_HOUR = 9;
    int MAX_WORK_HOUR = 17;

    List<String> SPECIALIZATIONS = Arrays.asList(
            "None",
            "Allergist",
            "Cardiologist",
            "Dentist",
            "Dermatologist",
            "Psychiatrist",
            "Radiologist",
            "Urologist"
    );

    List<String> getAppointments();
    void addToAppointments(String id);
    void setAppointments(List<String> appointments);

    String getSpecialization();
    void setSpecialization(String specialization);

    boolean getIsActive();
    void setIsActive(boolean isActive);
}
