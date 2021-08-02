package com.example.medical_clinic_app.user;

import com.example.medical_clinic_app.services.ClinicDao;

import java.time.LocalDateTime;
import java.util.List;

public interface Doctor extends User {
    List<Integer> getAppointments();
    List<Long> getAvailabilities(ClinicDao dao, LocalDateTime date);
}
