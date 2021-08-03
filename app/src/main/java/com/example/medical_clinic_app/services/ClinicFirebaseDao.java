package com.example.medical_clinic_app.services;

import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.time.UtcDateConverter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClinicFirebaseDao implements ClinicDao {
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final DatabaseReference doctorsRef = db.getReference("doctors");
    private static final DatabaseReference patientsRef = db.getReference("patients");
    private static final DatabaseReference appointmentsRef = db.getReference("appointments");

    @Override
    public DatabaseReference getDoctorsRef() {
        return doctorsRef;
    }

    @Override
    public DatabaseReference getPatientsRef() {
        return patientsRef;
    }

    @Override
    public DatabaseReference getAppointmentsRef() {
        return appointmentsRef;
    }

    @Override
    public DateConverter defaultDateConverter() {
        return new UtcDateConverter();
    }
}
