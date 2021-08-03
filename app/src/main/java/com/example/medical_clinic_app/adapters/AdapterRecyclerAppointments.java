package com.example.medical_clinic_app.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_clinic_app.R;
import com.example.medical_clinic_app.appointment.Appointment;
import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.services.ClinicFirebaseDao;
import com.example.medical_clinic_app.time.DateConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdapterRecyclerAppointments extends RecyclerView.Adapter<AdapterRecyclerAppointments.MyViewHolder> {
    private List<Appointment> appointments;

    public AdapterRecyclerAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate;
        private TextView txtTime;
        private TextView txtDoctor;
        private TextView txtPatient;

        public MyViewHolder(View view) {
            super(view);
            txtDate = view.findViewById(R.id.txtDate);
            txtTime = view.findViewById(R.id.txtTime);
            txtDoctor = view.findViewById(R.id.txtDoctor);
            txtPatient = view.findViewById(R.id.txtPatient);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerAppointments.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_appointment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    @SuppressLint("DefaultLocale")
    public void onBindViewHolder(@NonNull AdapterRecyclerAppointments.MyViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();
        LocalDateTime date = dateConverter.longToDate(appointment.getDate());
        holder.txtDate.setText(String.format("%s %d, %d", date.getMonth(), date.getDayOfMonth(), date.getYear()));
        holder.txtTime.setText(String.format("%02d:%02d", date.getHour(), date.getMinute()));
        holder.txtDoctor.setText(String.format("Dr. %s", appointment.getDoctor()));
        holder.txtPatient.setText(appointment.getPatient());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }
}
