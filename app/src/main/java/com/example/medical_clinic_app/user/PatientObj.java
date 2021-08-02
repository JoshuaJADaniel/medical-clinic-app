package com.example.medical_clinic_app.user;

import java.util.ArrayList;
import java.util.List;

public class PatientObj implements Patient{
    private int age;
    private String name;
    private String username;
    private String password;
    private List<Integer> appointments;

    public PatientObj(){

    }

    public PatientObj(String name, String username, String password, int age, List<Integer> appointments){
        this.age = age;
        this.name = name;
        this.username = username;
        this.password = password;
        this.appointments = appointments;
    }

    @Override
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public List<Integer> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Integer> appointments) {
        this.appointments = appointments;
    }

    @Override
    public void addAppointment(int appointmentId) {
        appointments.add(appointmentId);
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
