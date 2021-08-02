package com.example.medical_clinic_app.user;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;


import java.util.List;
import java.util.ArrayList;

public class DoctorObj implements Doctor{
    String name;
    String username;
    String password;
    ArrayList<Integer> appointments;



    public DoctorObj(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }


    @Override
    public List<Long> getAvailabilities(){


        return null;
    }

    @Override
    public List<Integer> getAppointments() {

        return appointments;
    }

    @Override
    public void addAppointment(int appointmentId) {
        appointments.add(appointmentId);
    }
}
