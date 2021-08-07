package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BookNewAppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_appointment);
    }

    //Haven't really worked with recyclerViews, not sure if this works properly for those
    //Also, need to transfer the name of the doctor selected to the textview on
    //the next page
    public void transferToDoctorAvailabilityList(View view) {
        Intent intent = new Intent(this, DoctorAvailabilityListActivity.class);
        startActivity(intent);
    }
}