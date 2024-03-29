package com.example.medical_clinic_app.utils;

import android.content.Context;
import android.widget.Toast;

public class CommonToasts {
    public static void databaseDoctorError(Context context) {
        databaseError(context, "Could not retrieve doctor from database");
    }

    public static void databasePatientError(Context context) {
        databaseError(context, "Could not retrieve patient from database");
    }

    public static void databaseAppointmentError(Context context) {
        databaseError(context, "Could not retrieve appointment from database");
    }

    private static void databaseError(Context context, String message) {
        Toast.makeText(context, String.format("%s! Please check your internet connection or try again later.", message), Toast.LENGTH_LONG).show();
    }

    public static void usernamePasswordError(Context context) {
        Toast.makeText(context, "Username or password is incorrect!", Toast.LENGTH_LONG).show();
    }

    public static void emptyFieldsError(Context context) {
        Toast.makeText(context, "One or more fields are empty!", Toast.LENGTH_LONG).show();
    }

    public static void usernameTaken(Context context) {
        Toast.makeText(context, "Sorry, that username is already taken!", Toast.LENGTH_LONG).show();
    }

    public static void loginSuccess(Context context, String username) {
        Toast.makeText(context, String.format("Successfully logged in as %s!", username), Toast.LENGTH_LONG).show();
    }

    public static void signupSuccess(Context context, String username) {
        Toast.makeText(context, String.format("Successfully signed up %s!", username), Toast.LENGTH_LONG).show();
    }

    public static void invalidDateEntered(Context context) {
        Toast.makeText(context, "Please enter a valid date!", Toast.LENGTH_LONG).show();
    }

    public static void unselectedAppointmentTime(Context context) {
        Toast.makeText(context, "Please select both an appointment date and time!", Toast.LENGTH_LONG).show();
    }

    public static void appointmentBookingError(Context context) {
        Toast.makeText(context, "Unable to book the appointment! Please try again later.", Toast.LENGTH_LONG).show();
    }

    public static void appointmentSuccess(Context context) {
        Toast.makeText(context, "Successfully booked appointment!", Toast.LENGTH_LONG).show();
    }
}
