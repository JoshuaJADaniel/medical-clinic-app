package com.example.medical_clinic_app.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_clinic_app.SelectTimeslotActivity;
import com.example.medical_clinic_app.user.Doctor;

import java.util.List;

import com.example.medical_clinic_app.R;

public class AdapterRecyclerPreviouslySeenDoctors extends RecyclerView.Adapter<AdapterRecyclerPreviouslySeenDoctors.MyViewHolder> {
    private final List<Doctor> doctors;
    private final String patient;

    public AdapterRecyclerPreviouslySeenDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
        this.patient = null;
    }

    public AdapterRecyclerPreviouslySeenDoctors(List<Doctor> doctors, String patient) {
        this.doctors = doctors;
        this.patient = patient;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtGender;
        private final TextView txtSpecialization;

        private String patient;
        private String doctor;

        public MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtGender = view.findViewById(R.id.txtGender);
            txtSpecialization = view.findViewById(R.id.txtSpecialization);


        }
    }

    @NonNull
    @Override
    public AdapterRecyclerPreviouslySeenDoctors.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_doctors, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerPreviouslySeenDoctors.MyViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.patient = patient;
        holder.doctor = doctor.getUsername();
        holder.txtGender.setText(doctor.getGender());
        holder.txtSpecialization.setText(doctor.getSpecialization());
        holder.txtName.setText(String.format("Dr. %s", doctor.getName()));
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }
}
