package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.widget.CalendarView;

import com.example.medical_clinic_app.time.UtcDateConverter;


import java.time.LocalDateTime;


public class DoctorTimeSlotActivity extends AppCompatActivity {
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private CalendarView calendarDaysDoctor;

    private String doctorUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_time_slot);
        Intent intent = getIntent();
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);


        calendarDaysDoctor = findViewById(R.id.calendarDaysDoctor);
        //calendarDaysDoctor.setMinDate(System.currentTimeMillis());
        //calendarDaysDoctor.setMaxDate(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

        calendarDaysDoctor.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                LocalDateTime t = LocalDateTime.of(year, month+1, dayOfMonth, 0, 0);
                UtcDateConverter utcDateConverter = new UtcDateConverter();
                long date = utcDateConverter.dateToLong(t);
                Intent intent2 = new Intent(DoctorTimeSlotActivity.this, DoctorViewDayTimeSlot.class);
                intent2.putExtra("date", date);
                intent2.putExtra(KEY_DOCTOR, doctorUsername);
                startActivity(intent2);

            }
        });


    }



}