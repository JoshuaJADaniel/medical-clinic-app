package com.example.medical_clinic_app.appointment;

public class GeneralAppointment implements Appointment {
    private long date;
    private String patient;
    private String doctor;

    public void Appointment() {

    }

    public void Appointment(long date, String patient, String doctor) {
        this.date = date;
        this.patient = patient;
        this.doctor = doctor;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String getDoctor() {
        return doctor;
    }

    @Override
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @Override
    public String getPatient() {
        return patient;
    }

    @Override
    public void setPatient(String patient) {
        this.patient = patient;
    }
}
