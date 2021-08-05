package com.example.medical_clinic_app.user;

import java.util.Arrays;
import java.util.List;

public class UserGenders {
    private static final List<String> genders = Arrays.asList(
            "Male",
            "Female",
            "Other"
    );

    public static List<String> getGenders() {
        return genders;
    }
}
