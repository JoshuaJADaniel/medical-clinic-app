package com.example.medical_clinic_app.services;

import com.google.firebase.database.DatabaseReference;

public interface ClinicDao {
    DatabaseReference getDoctorsRef();
    DatabaseReference getPatientsRef();
    DatabaseReference getAppointmentsRef();
}
