package com.example.medical_clinic_app.time;

import java.time.LocalDateTime;

public interface DateConverter {
    LocalDateTime longToDate(long time);
    long dateToLong(LocalDateTime time);

    String getFormattedDate(long time);
    String getFormattedDate(LocalDateTime time);

    String getFormattedTime(long time);
    String getFormattedTime(LocalDateTime time);

    String getLocale();
}
