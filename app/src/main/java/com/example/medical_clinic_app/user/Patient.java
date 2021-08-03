package com.example.medical_clinic_app.user;

import java.util.List;

public interface Patient extends User {
    int getAge();
    void setAge(int age);

    List<Integer> getAppointments();
    void setAppointments(List<Integer> appointments);
}
