package com.example.medical_clinic_app.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.appointment.GeneralAppointment;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.time.UtcDateConverter;
import com.example.medical_clinic_app.user.DoctorObj;
import com.example.medical_clinic_app.user.PatientObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void getDoctor(String username, DoctorListener listener) {
        doctorsRef.child(parseUsername(username)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.callback(snapshot.getValue(DoctorObj.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.callback(null);
                Log.e(null, error.toString());
                Log.e(null, String.format("Error reading doctor with username %s", username));
            }
        });
    }

    @Override
    public void getPatient(String username, PatientListener listener) {
        patientsRef.child(parseUsername(username)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.callback(snapshot.getValue(PatientObj.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.callback(null);
                Log.e(null, error.toString());
                Log.e(null, String.format("Error reading patient with username %s", username));
            }
        });
    }

    @Override
    public void getAppointment(String id, AppointmentListener listener) {
        appointmentsRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.callback(snapshot.getValue(GeneralAppointment.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.callback(null);
                Log.e(null, error.toString());
                Log.e(null, String.format("Error reading appointment with id %s", id));
            }
        });
    }

    @Override
    public boolean addAppointment(Appointment appointment) {
        try {
            getPatient(appointment.getPatient(), patient -> {
                getDoctor(appointment.getDoctor(), doctor -> {
                    appointmentsRef.push().setValue(appointment);
                    String appointmentId = appointmentsRef.getKey();

                    doctor.addToAppointments(appointmentId);
                    patient.addToAppointments(appointmentId);
                    doctorsRef.child(doctor.getUsername()).setValue(doctor);
                    patientsRef.child(patient.getUsername()).setValue(patient);
                });
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void validateUsername(String username) throws Exception {
        int minLength = 3, maxLength = 32;
        username = parseUsername(username);
        if (username.length() < minLength || username.length() > maxLength) {
            throw new Exception(String.format("Username must be %d-%d characters long!", minLength, maxLength));
        } else if (!username.matches("[a-z0-9]+")) {
            throw new Exception("Username must be alphanumeric!");
        }
    }

    @Override
    public DateConverter defaultDateConverter() {
        return new UtcDateConverter();
    }

    private String parseUsername(String username) {
        return username.toLowerCase().trim();
    }
}
