package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.utils.CommonToasts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DoctorScheduleActivity extends AppCompatActivity {
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private Toolbar toolbar;

    private TextView txtSchedulePrompt;
    private LinearLayout linearScheduleLeft;
    private LinearLayout linearScheduleRight;

    private String doctorUsername;
    private final List<Appointment> appointmentList = new ArrayList<>();
    private final Map<Integer, TextView> hourToScheduleText = new HashMap<>();

    private final LocalDateTime now = LocalDateTime.now();
    private final ClinicDao dao = new ClinicFirebaseDao();
    private final DateConverter dateConverter = dao.defaultDateConverter();

    private static final String statusFree = "Free";
    private static final String statusBooked = "Booked";

    private final CalendarView.OnDateChangeListener dateChangeListener = (calendarView, year, month, day) -> {
        for (int hour = Doctor.MIN_WORK_HOUR; hour < Doctor.MAX_WORK_HOUR; hour++) {
            Objects.requireNonNull(hourToScheduleText.get(hour)).setText(formatSchedule(hour, statusFree));
        }

        LocalDateTime selectedDate = LocalDateTime.of(year, month+1, day, 0, 0);
        txtSchedulePrompt.setText(formatPrompt(selectedDate));

        for (Appointment appointment : appointmentList) {
            LocalDateTime appointmentDate = dateConverter.longToDate(appointment.getDate());
            if (onSameDate(appointmentDate, selectedDate)) {
                Objects.requireNonNull(hourToScheduleText.get(appointmentDate.getHour())).setText(formatSchedule(appointmentDate.getHour(), statusBooked));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule);

        Intent intent = getIntent();
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);

        toolbar = findViewById(R.id.toolbarSchedule);
        linearScheduleLeft = findViewById(R.id.linearScheduleLeft);
        linearScheduleRight = findViewById(R.id.linearScheduleRight);
        txtSchedulePrompt = findViewById(R.id.txtSchedulePrompt);
        txtSchedulePrompt.setText(formatPrompt(now));

        initializeToolbar();
        initializeLinearHours();
        initializeAppointments();

        long millisecondsPerWeek = 7 * 24 * 60 * 60 * 1000;
        CalendarView calendar = findViewById(R.id.calendarSchedule);
        calendar.setMaxDate(System.currentTimeMillis() + millisecondsPerWeek);
        calendar.setMinDate(System.currentTimeMillis());
        calendar.setOnDateChangeListener(dateChangeListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);
    }

    @SuppressLint("DefaultLocale")
    private void initializeLinearHours() {
        int centerHour = (Doctor.MIN_WORK_HOUR + Doctor.MAX_WORK_HOUR) / 2;
        for (int hour = Doctor.MIN_WORK_HOUR; hour < Doctor.MAX_WORK_HOUR; hour++) {
            TextView txtHourSchedule = new TextView(this);
            txtHourSchedule.setText(formatSchedule(hour, statusFree));
            txtHourSchedule.setPadding(0, 10, 0, 10);
            txtHourSchedule.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            (hour < centerHour ? linearScheduleLeft : linearScheduleRight).addView(txtHourSchedule);
            hourToScheduleText.put(hour, txtHourSchedule);
        }
    }

    private void initializeAppointments() {
        dao.getDoctor(doctorUsername, doctor -> {
            if (doctor == null) {
                CommonToasts.databaseDoctorError(DoctorScheduleActivity.this);
                return;
            }

            if (doctor.getAppointments() == null) {
                return;
            }

            for (String id : doctor.getAppointments()) {
                dao.getAppointment(id, appointment -> {
                    if (appointment == null) {
                        CommonToasts.databaseAppointmentError(DoctorScheduleActivity.this);
                        return;
                    }

                    appointmentList.add(appointment);
                    LocalDateTime appointmentDate = dateConverter.longToDate(appointment.getDate());
                    if (onSameDate(appointmentDate, now)) {
                        Objects.requireNonNull(hourToScheduleText.get(appointmentDate.getHour())).setText(
                                formatSchedule(appointmentDate.getHour(), statusBooked)
                        );
                    }
                });
            }
        });
    }

    private boolean onSameDate(LocalDateTime date1, LocalDateTime date2) {
        return (date1.getYear() == date2.getYear() &&
                date1.getMonthValue() == date2.getMonthValue() &&
                date1.getDayOfMonth() == date2.getDayOfMonth());
    }

    private String formatPrompt(LocalDateTime date) {
        return String.format("Schedule for %s", dateConverter.getFormattedDate(date));
    }

    @SuppressLint("DefaultLocale")
    private String formatSchedule(int hour, String status) {
        return String.format("%02d:00 - %s", hour, status);
    }
}