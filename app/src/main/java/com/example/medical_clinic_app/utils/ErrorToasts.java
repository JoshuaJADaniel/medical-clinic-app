package com.example.medical_clinic_app.utils;

import android.content.Context;
import android.widget.Toast;

public class ErrorToasts {
    public static void databaseToast(Context context) {
        Toast.makeText(context, "Something went wrong accessing the database! Please check your internet connection or try again later", Toast.LENGTH_LONG).show();
    }
}
