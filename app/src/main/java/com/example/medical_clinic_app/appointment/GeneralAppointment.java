package com.example.medical_clinic_app.appointment;

public class GeneralAppointment implements Appointment{

    long date;
    String patient;
    String doctor;

    public void Appointment(){

        date = 0L;
        patient = "";
        doctor = "";
    }

    public void Appointment(long date, String patient, String doctor){

        this.date = date;
        this.patient = patient;
        this.doctor = doctor;
    }
    @Override
    public long getTime() {
        return date;
    }

    @Override
    public String getDoctor() {
        return doctor;
    }

    @Override
    public String getPatient() {
        return patient;
    }

    public void setDate(long date){

        this.date = date;
    }

    public void setPatient(String patient){

        this.patient = patient;
    }

    public void setDoctor(String doctor){

        this.doctor = doctor;
    }
}
