package com.example.medical_clinic_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_clinic_app.AppointmentViewActivity;
import com.example.medical_clinic_app.DoctorTimeSlotActivity;
import com.example.medical_clinic_app.R;
import com.example.medical_clinic_app.appointment.Appointment;

public class AdapterrecyclerTimeSlotDoctor extends RecyclerView.Adapter<AdapterrecyclerTimeSlotDoctor.MyViewHolder> {
    private Context mContext;
    private Appointment[] appointments;

    public AdapterrecyclerTimeSlotDoctor(Context context, Appointment[] appointments) {
        this.mContext = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AdapterrecyclerTimeSlotDoctor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_timeslot, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterrecyclerTimeSlotDoctor.MyViewHolder holder, int position) {
        String time = (9 + position) + ":00 UTC";
        holder.timeSlot.setText(time);
        if (appointments[position] == null)
            holder.status.setText("free");
        holder.status.setText(appointments[position].getDoctor());
    }

    @Override
    public int getItemCount() {
        return appointments.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView timeSlot;
        private TextView status;

        public MyViewHolder(View view) {
            super(view);
            timeSlot = view.findViewById(R.id.titleTimeSlotDoctor);
            status = view.findViewById(R.id.StatusDoctor);
        }
    }
}
