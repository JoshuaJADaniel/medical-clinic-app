package com.example.medical_clinic_app.user;

import java.util.Arrays;
import java.util.List;

public class DoctorSpecializations {
    private static final List<String> specializations = Arrays.asList(
            "None",
            "Allergist",
            "Cardiologist",
            "Dentist",
            "Dermatologist",
            "Psychiatrist",
            "Radiologist",
            "Urologist"
    );

    public static List<String> getSpecializations() {
        return specializations;
    }
}
