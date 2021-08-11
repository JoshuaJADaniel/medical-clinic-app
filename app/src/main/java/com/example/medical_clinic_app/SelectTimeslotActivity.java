package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.utils.CommonToasts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectTimeslotActivity extends AppCompatActivity {
    public static final String KEY_PATIENT = "KEY_PATIENT";
    public static final String KEY_DOCTOR = "KEY_DOCTOR";

    private String patientUsername;
    private String doctorUsername;

    private final LocalDateTime now = LocalDateTime.now();
    private final ClinicDao dao = new ClinicFirebaseDao();
    private final DateConverter dateConverter = dao.defaultDateConverter();

    private Integer selectedHour;
    private LocalDateTime selectedDate;
    private final List<Appointment> appointmentList = new ArrayList<>();
    private final Map<Integer, RadioButton> hoursToButtons = new HashMap<>();

    private Toolbar toolbar;
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

    private final CalendarView.OnDateChangeListener dateChangeListener = (calendarView, year, month, day) -> {
        selectedDate = LocalDateTime.of(year, month + 1, day, 0, 0);
        radioGroupRight.clearCheck();
        radioGroupLeft.clearCheck();
        selectedHour = null;

        for (int hour = Doctor.MIN_WORK_HOUR; hour < Doctor.MAX_WORK_HOUR; hour++) {
            Objects.requireNonNull(hoursToButtons.get(hour)).setEnabled(true);
        }

        for (Appointment appointment : appointmentList) {
            LocalDateTime appointmentDate = dateConverter.longToDate(appointment.getDate());
            if (onSameDate(appointmentDate, selectedDate)) {
                Objects.requireNonNull(hoursToButtons.get(appointmentDate.getHour())).setEnabled(false);
            }
        }

        if (onSameDate(selectedDate, now)) {
            for (int hour = Doctor.MIN_WORK_HOUR; hour < Doctor.MAX_WORK_HOUR; hour++) {
                if (hour <= now.getHour()) {
                    Objects.requireNonNull(hoursToButtons.get(hour)).setEnabled(false);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_timeslot);

        Intent intent = getIntent();
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);
        patientUsername = intent.getStringExtra(KEY_PATIENT);

        toolbar = findViewById(R.id.toolbarSelectTimeslot);
        radioGroupLeft = findViewById(R.id.radioGroupLeft);
        radioGroupRight = findViewById(R.id.radioGroupRight);

        initializeToolbar();
        initializeRadioGroups();
        initializeAppointments();
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
    private void initializeRadioGroups() {
        int centerHour = (Doctor.MIN_WORK_HOUR + Doctor.MAX_WORK_HOUR) / 2;
        for (int hour = Doctor.MIN_WORK_HOUR; hour < Doctor.MAX_WORK_HOUR; hour++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(String.format("%02d:00", hour));
            radioButton.setTag(hour);

            (hour < centerHour ? radioGroupLeft : radioGroupRight).addView(radioButton);
            if (hour <= now.getHour()) radioButton.setEnabled(false);
            hoursToButtons.put(hour, radioButton);
        }

        radioGroupLeft.setOnCheckedChangeListener(listenerLeft);
        radioGroupRight.setOnCheckedChangeListener(listenerRight);

        long millisecondsPerWeek = 7 * 24 * 60 * 60 * 1000;
        CalendarView calendar = findViewById(R.id.calendarDays);
        calendar.setMaxDate(System.currentTimeMillis() + millisecondsPerWeek);
        calendar.setMinDate(System.currentTimeMillis());
        calendar.setOnDateChangeListener(dateChangeListener);
    }

    private void initializeAppointments() {
        dao.getDoctor(doctorUsername, doctor -> {
            if (doctor == null) {
                CommonToasts.databaseDoctorError(SelectTimeslotActivity.this);
                return;
            }

            if (doctor.getAppointments() == null) {
                return;
            }

            for (String id : doctor.getAppointments()) {
                dao.getAppointment(id, appointment -> {
                    if (appointment == null) {
                        CommonToasts.databaseAppointmentError(SelectTimeslotActivity.this);
                        return;
                    }

                    appointmentList.add(appointment);
                    LocalDateTime appointmentDate = dateConverter.longToDate(appointment.getDate());
                    if (onSameDate(appointmentDate, now)) {
                        Objects.requireNonNull(hoursToButtons.get(appointmentDate.getHour())).setEnabled(false);
                    }
                });
            }
        });
    }

    private void handleRadioButton(int viewId) {
        RadioButton radioButton = findViewById(viewId);
        selectedHour = (Integer) radioButton.getTag();
    }

    public void bookAppointment(View view) {
        if (selectedDate == null || selectedHour == null) {
            CommonToasts.unselectedAppointmentTime(this);
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

        Appointment appointment = new GeneralAppointment(
                dateConverter.dateToLong(selectedDateAndTime),
                patientUsername,
                doctorUsername
        );

        dao.addAppointment(appointment, returnedAppointment -> {
            if (returnedAppointment == null) {
                CommonToasts.appointmentBookingError(SelectTimeslotActivity.this);
            } else {
                CommonToasts.appointmentSuccess(SelectTimeslotActivity.this);
                Intent intent = new Intent(SelectTimeslotActivity.this, PatientDashboardActivity.class);
                intent.putExtra(PatientDashboardActivity.KEY_PATIENT, patientUsername);
                startActivity(intent);
            }
        });
    }

    private boolean onSameDate(LocalDateTime date1, LocalDateTime date2) {
        return (date1.getYear() == date2.getYear() &&
                date1.getMonthValue() == date2.getMonthValue() &&
                date1.getDayOfMonth() == date2.getDayOfMonth());
    }
}
