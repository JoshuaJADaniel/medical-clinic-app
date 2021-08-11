package com.example.medical_clinic_app.user;

import java.util.ArrayList;
import java.util.List;

public class PatientObj implements Patient {
    private String name;
    private String gender;
    private String username;
    private String password;
    private long dateOfBirth;
    private List<String> appointments;

    public PatientObj() {

    }

    public PatientObj(String name,
                      String gender,
                      String username,
                      String password,
                      long dateOfBirth,
                      List<String> appointments) {
        this.name = name;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.appointments = appointments;
    }

    @Override
    public long getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public List<String> getAppointments() {
        return appointments;
    }

    @Override
    public void setAppointments(List<String> appointments) {
        this.appointments = appointments;
    }

    @Override
    public void addToAppointments(String id) {
        if (appointments == null) appointments = new ArrayList<>();
        appointments.add(id);
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PatientObj{" +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", appointments=" + appointments +
                '}';
    }
}
