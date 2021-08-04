package com.example.medical_clinic_app;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>{
    private ArrayList<Appointments> appList;

    public recyclerAdapter(ArrayList<Appointments> appointmentList){
        this.appList = appointmentList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(final View view) {
            super(view);
            nameTxt = view.findViewById(R.id.textView3); // Patient


        }
    }


    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String patient = appList.get(position).getPatient();
        String doctor = appList.get(position).getDoctor();



    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
