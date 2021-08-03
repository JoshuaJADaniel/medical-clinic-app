package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

public class Appointments extends AppCompatActivity {
    private String Patient;
    private String Doctor;
    private int Time;

    public String getPatient() {
        return Patient;
    }

    public void setPatient(String patient) {
        Patient = patient;
    }

    public String getDoctor() {
        return Doctor;
    }

    public void setDoctor(String doctor) {
        Doctor = doctor;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }
}
