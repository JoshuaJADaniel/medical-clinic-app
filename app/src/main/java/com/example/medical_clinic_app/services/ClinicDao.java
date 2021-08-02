package com.example.medical_clinic_app.services;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.user.Patient;

public interface ClinicDao {
    Patient findPatientByUsername(String username);
    Doctor findDoctorByUsername(String username);
    Appointment findAppointmentById(int id);
    DateConverter getDateConverter();

    void addPatient(Patient patient);
    void addDoctor(Doctor doctor);
    void addAppointment(Appointment appointment);
}
