package com.example.medical_clinic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.medical_clinic_app.adapters.AdapterrecyclerTimeSlotDoctor;
import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.utils.CommonToasts;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewDayTimeSlot extends AppCompatActivity {
    public static final String KEY_DOCTOR = "KEY_DOCTOR";
    private RecyclerView recyclerTimeSlotDoctor;
    private String doctorUsername;
    private long tmp;
    private List<Appointment> upcomingAppointments;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_day_time_slot);
        textView = findViewById(R.id.textViewLoding);
        upcomingAppointments = new ArrayList<Appointment>();
        for(int i = 0; i < 8; ++i){
            upcomingAppointments.add(null);
        }
        Intent intent = getIntent();
        doctorUsername = intent.getStringExtra(KEY_DOCTOR);
        long date = intent.getLongExtra("date", 0);

        recyclerTimeSlotDoctor = findViewById(R.id.recyclerTimeSlotDoctor);
        recyclerTimeSlotDoctor.setLayoutManager(new LinearLayoutManager(DoctorViewDayTimeSlot.this));
        RecyclerView.Adapter adapter = new AdapterrecyclerTimeSlotDoctor(DoctorViewDayTimeSlot.this, upcomingAppointments);
        recyclerTimeSlotDoctor.setAdapter(adapter);

        ClinicDao dao = new ClinicFirebaseDao();

        dao.getDoctor(doctorUsername, doctor -> {
            textView.setText("");
            if (doctor.getAppointments() == null) {
                CommonToasts.databasePatientError(DoctorViewDayTimeSlot.this);
                return;
            }
            for (String id : doctor.getAppointments()) {
                dao.getAppointment(id, appointment -> {
                    if (appointment == null) {
                        CommonToasts.databaseAppointmentError(DoctorViewDayTimeSlot.this);
                        return;
                    }
                    if ((appointment.getDate() >= date) && (appointment.getDate() <= date + 24 * 3600)){
                        int i = (int) ((appointment.getDate() - date)/3600) - 9;
                        upcomingAppointments.set(i, appointment);
                        adapter.notifyItemChanged(i);
                    }


                });
            }
            textView.setText("");
        });





    }


}
