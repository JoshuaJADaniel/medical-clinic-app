package com.example.medical_clinic_app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.user.Doctor;

import java.util.List;

import com.example.medical_clinic_app.R;


public class AdapterRecyclerDoctorsAvailable extends RecyclerView.Adapter<AdapterRecyclerDoctorsAvailable.MyViewHolder> {
    private final List<Doctor> doctors;

    public AdapterRecyclerDoctorsAvailable(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtName;
        private final TextView txtSpecialties;
        private final TextView txtGender;

        public MyViewHolder(View view){
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtSpecialties = view.findViewById(R.id.txtSpecialties);
            txtGender = view.findViewById(R.id.txtGender);
        }

    }
    @NonNull
    @Override
    public AdapterRecyclerDoctorsAvailable.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_doctors_available,parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerDoctorsAvailable.MyViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);

        holder.txtName.setText(doctor.getName());
        holder.txtGender.setText(doctor.getGender());
        holder.txtSpecialties.setText(doctor.getSpecialization());


    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }
}
