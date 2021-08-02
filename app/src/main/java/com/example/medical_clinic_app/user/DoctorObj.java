package com.example.medical_clinic_app.user;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class DoctorObj implements Doctor {
    private String name;
    private String username;
    private String password;
    private List<Integer> appointments;

    // 9-5 work hours based on 24 hour format
    private static final int START_HOUR = 9;
    private static final int FINAL_HOUR = 17;

    private DoctorObj() {

    }

    public DoctorObj(String name, String username, String password, List<Integer> appointments) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.appointments = appointments;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<Integer> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Integer> appointments) {
        this.appointments = appointments;
    }
}
