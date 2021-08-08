package com.example.medical_clinic_app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_clinic_app.DoctorAvailableTimeSlotActivity;
import com.example.medical_clinic_app.user.Doctor;

import java.util.List;

import com.example.medical_clinic_app.R;
import android.content.Intent;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_clinic_app.AppointmentViewActivity;
import com.example.medical_clinic_app.R;
import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;
import com.example.medical_clinic_app.utils.FormatUsersAppointmentStrategy;

import java.util.List;
public class AdapterRecyclerDoctorsAvailable extends RecyclerView.Adapter<AdapterRecyclerDoctorsAvailable.MyViewHolder> {
    private final List<Doctor> doctors;
    private static String patientString = "KEY_PATIENT";

    public AdapterRecyclerDoctorsAvailable(List<Doctor> doctors, String patientString) {
        this.doctors = doctors;
        this.patientString = patientString;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtGender;
        private final TextView txtSpecialties;

        private String doctorString;


        public MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtGender = view.findViewById(R.id.txtGender);
            txtSpecialties = view.findViewById(R.id.txtSpecialties);

            view.setOnClickListener(innerView -> {
                Intent intent = new Intent(view.getContext(), DoctorAvailableTimeSlotActivity.class);
                //intent.putExtra(DoctorAvailableTimeSlotActivity.KEY_DOCTOR, doctorString);
                //intent.putExtra(DoctorAvailableTimeSlotActivity.KEY_PATIENT, patientString);
                view.getContext().startActivity(intent);
            });

        }
    }

    @NonNull
    @Override
    public AdapterRecyclerDoctorsAvailable.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_doctors, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerDoctorsAvailable.MyViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.doctorString = doctor.getName().trim();
        holder.txtName.setText(doctor.getName());
        holder.txtGender.setText(doctor.getGender());
        holder.txtSpecialties.setText(doctor.getSpecialization());
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }
}
