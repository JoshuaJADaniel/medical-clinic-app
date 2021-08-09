package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.medical_clinic_app.adapters.AdapterrecyclerTimeSlotDoctor;
import com.example.medical_clinic_app.time.UtcDateConverter;
import com.example.medical_clinic_app.utils.FormatPatientsAppointment;

import java.time.LocalDateTime;
import java.util.Date;

public class DoctorTimeSlotActivity extends AppCompatActivity {
    public static final String KEY_PATIENT = "KEY_PATIENT";

    private String patientUsername;
    private RecyclerView recyclerTimeSlotDoctor;
    private CalendarView calendarDaysDoctor;




    private String doctorUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        patientUsername = intent.getStringExtra(KEY_PATIENT);
        setContentView(R.layout.activity_doctor_time_slot);
        recyclerTimeSlotDoctor = findViewById(R.id.recyclerTimeSlotDoctor);
        recyclerTimeSlotDoctor.setLayoutManager(new LinearLayoutManager(this));
        calendarDaysDoctor = findViewById(R.id.calendarDaysDoctor);
        calendarDaysDoctor.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                LocalDateTime t = LocalDateTime.of(year,month + 1,dayOfMonth,0,0);
                UtcDateConverter utcDateConverter = new UtcDateConverter();
                long date = utcDateConverter.dateToLong(t);
                //uncompleted
            }
        });

        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {

//        adapterrecyclerTimeSlotDoctor = new AdapterrecyclerTimeSlotDoctor(visibleAppointments, new FormatPatientsAppointment());
        //   recyclerTimeSlotDoctor.setAdapter(DoctorTimeSlotActivity.this, );
//        recyclerTimeSlotDoctor.setItemAnimator(new DefaultItemAnimator());

    }


}