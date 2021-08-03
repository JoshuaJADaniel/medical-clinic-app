package com.example.medical_clinic_app.user;

import java.util.List;

public class DoctorObj implements Doctor {
    private String name;
    private String username;
    private String password;
    private List<Integer> appointments;

    public DoctorObj() {

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

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<Integer> getAppointments() {
        return appointments;
    }

    @Override
    public void setAppointments(List<Integer> appointments) {
        this.appointments = appointments;
    }
}
