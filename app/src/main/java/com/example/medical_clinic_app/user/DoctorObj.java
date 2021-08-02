package com.example.medical_clinic_app.user;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class DoctorObj implements Doctor{
    String name;
    String username;
    String password;
    ArrayList<Integer> appointments;
    List<Long> availability;
    /*
    Doctor's schedule should be 8am-5pm Monday to Friday
                                8am-12am on weekends
     */



    public DoctorObj(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }



    @Override
    public List<Long> getAvailabilities(){
        for(Integer appInt: appointments){


        }




        return null;
    }

    @Override
    public List<Integer> getAppointments() {

        return appointments;
    }

    @Override
    public void addAppointment(int appointmentId) {
        appointments.add(appointmentId);
        long cur=-100000;
        long prev=-100000;
        Iterator<Long> iterator = null;
        iterator = availability.iterator();
        int i=0;
        while(iterator.hasNext()){
            prev = cur;
            cur = iterator.next();

            i++;


        }
        //will the Dao App storing Availability as well?
    }
}
