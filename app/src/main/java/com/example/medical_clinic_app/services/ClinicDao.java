package com.example.medical_clinic_app.services;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.user.Patient;
import com.google.firebase.database.DatabaseReference;

public interface ClinicDao {
    interface PatientListener {
        void callback(Patient patient);
    }

    interface DoctorListener {
        void callback(Doctor doctor);
    }

    interface AppointmentListener {
        void callback(Appointment appointment);
    }

    DatabaseReference getDoctorsRef();
    DatabaseReference getPatientsRef();
    DatabaseReference getAppointmentsRef();

    void getDoctor(String username, DoctorListener listener);
    void getPatient(String username, PatientListener listener);
    void getAppointment(String id, AppointmentListener listener);

    boolean addAppointment(Appointment appointment);

    void validateUsername(String username) throws Exception;

    DateConverter defaultDateConverter();
}
