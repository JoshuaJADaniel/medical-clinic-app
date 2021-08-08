package com.example.medical_clinic_app.adapters;

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

public class AdapterRecyclerAppointments extends RecyclerView.Adapter<AdapterRecyclerAppointments.MyViewHolder> {
    private final List<Appointment> appointments;
    private final FormatUsersAppointmentStrategy userFormatStrategy;

    public AdapterRecyclerAppointments(List<Appointment> appointments, FormatUsersAppointmentStrategy userFormatStrategy) {
        this.appointments = appointments;
        this.userFormatStrategy = userFormatStrategy;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDate;
        private final TextView txtTime;
        private final TextView txtUser;

        private long date;
        private String doctor;
        private String patient;

        public MyViewHolder(View view) {
            super(view);

            txtDate = view.findViewById(R.id.txtDate);
            txtTime = view.findViewById(R.id.txtTime);
            txtUser = view.findViewById(R.id.txtUser);

            view.setOnClickListener(innerView -> {

                    Intent intent = new Intent(view.getContext(), AppointmentViewActivity.class);

                    intent.putExtra(AppointmentViewActivity.KEY_PATIENT, patient);
                    intent.putExtra(AppointmentViewActivity.KEY_DOCTOR, doctor);
                    intent.putExtra(AppointmentViewActivity.KEY_TIME, date);
                    view.getContext().startActivity(intent);



            });
        }
    }

    @NonNull
    public AdapterRecyclerAppointments.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_appointment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerAppointments.MyViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.date = appointment.getDate();
        holder.doctor = appointment.getDoctor();
        holder.patient = appointment.getPatient();

        ClinicDao dao = new ClinicFirebaseDao();
        DateConverter dateConverter = dao.defaultDateConverter();
        holder.txtDate.setText(dateConverter.getFormattedDate(appointment.getDate()));
        holder.txtTime.setText(dateConverter.getFormattedTime(appointment.getDate()));
        userFormatStrategy.formatTxtUser(holder.txtUser, appointment);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }
}