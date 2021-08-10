package com.example.medical_clinic_app.adapters;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_clinic_app.AppointmentViewActivity;
import com.example.medical_clinic_app.R;
import com.example.medical_clinic_app.appointment.Appointment;

import java.util.List;

public class AdapterrecyclerTimeSlotDoctor extends RecyclerView.Adapter<AdapterrecyclerTimeSlotDoctor.MyViewHolder> {
    private final Context mContext;
    private final List<Appointment> appointments;
    public AdapterrecyclerTimeSlotDoctor(Context context, List<Appointment> appointments) {
        this.mContext = context;
        this.appointments = appointments;
    }
    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView timeSlot;
        private final TextView status;

        private long date;
        private String doctor;
        private String patient;
        private boolean clickable;

        public MyViewHolder(View view) {
            super(view);
            timeSlot = view.findViewById(R.id.TimeSlotDoctorxdsee);
            status = view.findViewById(R.id.StatusDoctorxdsee);

            view.setOnClickListener(innerView -> {
                if (clickable){
                    Intent intent = new Intent(view.getContext(), AppointmentViewActivity.class);

                    intent.putExtra(AppointmentViewActivity.KEY_IS_DOCTOR, true);
                    intent.putExtra(AppointmentViewActivity.KEY_PATIENT, patient);
                    intent.putExtra(AppointmentViewActivity.KEY_DOCTOR, doctor);
                    intent.putExtra(AppointmentViewActivity.KEY_TIME, date);
                    view.getContext().startActivity(intent);

                }


            });
        }
    }



    @NonNull
    @Override
    public AdapterrecyclerTimeSlotDoctor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_timeslot, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterrecyclerTimeSlotDoctor.MyViewHolder holder, int position) {
        String time = (position + 9) + ":00 UTC";
        holder.timeSlot.setText(time);
        Appointment appointment = appointments.get(position);




        if (appointment != null){
            holder.clickable = true;
            holder.date = appointment.getDate();
            holder.doctor = appointment.getDoctor();
            holder.patient = appointment.getPatient();
            holder.status.setText(appointment.getPatient());
            holder.status.setBackgroundColor(Color.parseColor("#2962FF"));
            holder.timeSlot.setBackgroundColor(Color.parseColor("#2962FF"));
            holder.timeSlot.setTextColor(Color.parseColor("#FFFFFF"));
            holder.status.setTextColor(Color.parseColor("#FFFFFF"));

        }
        else {
            holder.clickable = false;
            holder.status.setText("free");
            holder.timeSlot.setBackgroundColor(Color.parseColor("#FFFFFF"));holder.timeSlot.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.status.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.timeSlot.setTextColor(Color.parseColor("#000000"));
            holder.status.setTextColor(Color.parseColor("#000000"));
        }
    }


}
