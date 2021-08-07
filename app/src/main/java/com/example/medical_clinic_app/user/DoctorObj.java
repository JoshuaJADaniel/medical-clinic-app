package com.example.medical_clinic_app.user;

import androidx.annotation.NonNull;

import java.util.List;

public class DoctorObj implements Doctor {
    private String name;
    private String gender;
    private String username;
    private String password;
    private String specialization;
    private List<String> appointments;
    private boolean isActive;

    public DoctorObj() {

    }

    public DoctorObj(String name,
                     String gender,
                     String username,
                     String password,
                     String specialization,
                     List<String> appointments,
                     boolean isActive) {
        this.name = name;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.specialization = specialization;
        this.appointments = appointments;
        this.isActive = isActive;
    }

    @Override
    public String getSpecialization() {
        return specialization;
    }

    @Override
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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
    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
    public List<String> getAppointments() {
        return appointments;
    }

    @Override
    public void setAppointments(List<String> appointments) {
        this.appointments = appointments;
    }

    @Override
    public void addToAppointments(String id) {
        appointments.add(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "DoctorObj{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", specialization='" + specialization + '\'' +
                ", appointments=" + appointments +
                ", isActive=" + isActive +
                '}';
    }
}
