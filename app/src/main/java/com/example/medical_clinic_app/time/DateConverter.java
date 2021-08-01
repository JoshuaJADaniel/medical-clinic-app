package com.example.medical_clinic_app.time;

import java.time.LocalDateTime;

public interface DateConverter {
    LocalDateTime longToDate(long time);
    long dateToLong(LocalDateTime time);
}
