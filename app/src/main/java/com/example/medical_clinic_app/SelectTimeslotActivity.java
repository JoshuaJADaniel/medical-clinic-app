package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.appointment.GeneralAppointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SelectTimeslotActivity extends AppCompatActivity {
    public static final String KEY_PATIENT = "KEY_PATIENT";
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private String patientUsername;
    private String doctorUsername;

    private Integer selectedHour;
    private LocalDateTime selectedDate;
    private Map<Integer, RadioButton> hoursToButtons;

    private RadioGroup radioGroupLeft;
    private RadioGroup radioGroupRight;

    private final RadioGroup.OnCheckedChangeListener listenerRight = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                radioGroupLeft.setOnCheckedChangeListener(null);
                radioGroupLeft.clearCheck();
                radioGroupLeft.setOnCheckedChangeListener(listenerLeft);
                handleRadioButton(checkedId);
            }
        }
    };

    private final RadioGroup.OnCheckedChangeListener listenerLeft = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                radioGroupRight.setOnCheckedChangeListener(null);
                radioGroupRight.clearCheck();
                radioGroupRight.setOnCheckedChangeListener(listenerRight);
                handleRadioButton(checkedId);
            }
        }
    };

    @Override
    @SuppressLint("DefaultLocale")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_timeslot);

        Intent intent = getIntent();
        patientUsername = intent.getStringExtra(KEY_PATIENT);
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);

        hoursToButtons = new HashMap<>();
        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();

        radioGroupLeft = findViewById(R.id.radioGroupLeft);
        radioGroupRight = findViewById(R.id.radioGroupRight);

        int minHour = 9, maxHour = 17, centerHour = (minHour + maxHour) / 2;
        for (int hour = minHour; hour < maxHour; hour++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(String.format("%02d:00 %s", hour, dateConverter.getLocale()));
            (hour < centerHour ? radioGroupLeft : radioGroupRight).addView(radioButton);
            radioButton.setTag(hour);
        }

        radioGroupLeft.setOnCheckedChangeListener(listenerLeft);
        radioGroupRight.setOnCheckedChangeListener(listenerRight);

        CalendarView calendar = findViewById(R.id.calendarDays);
        calendar.setMinDate(System.currentTimeMillis());
        calendar.setMaxDate(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        calendar.setOnDateChangeListener((calendarView, year, month, day) -> {
            selectedDate = LocalDateTime.of(year, month + 1, day, 0, 0);
        });
    }

    private void handleRadioButton(int viewId) {
        RadioButton radioButton = findViewById(viewId);
        selectedHour = (Integer) radioButton.getTag();
    }

    public void bookAppointment(View view) {
        if (selectedDate == null || selectedHour == null) {
            return;
        }

        LocalDateTime selectedDateAndTime = LocalDateTime.of(
                selectedDate.getYear(),
                selectedDate.getMonthValue(),
                selectedDate.getDayOfMonth(),
                selectedHour,
                0,
                0
        );

        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();
        Appointment appointment = new GeneralAppointment(
                dateConverter.dateToLong(selectedDateAndTime),
                patientUsername,
                doctorUsername
        );
        dao.addAppointment(appointment);
    }
}
