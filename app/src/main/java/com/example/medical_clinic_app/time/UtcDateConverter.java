package com.example.medical_clinic_app.time;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UtcDateConverter implements DateConverter {
    @Override
    public LocalDateTime longToDate(long time) {
        return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
    }

    @Override
    public long dateToLong(LocalDateTime time) {
        return time.toEpochSecond(ZoneOffset.UTC);
    }

    @Override
    public String getFormattedDate(long time) {
        return getFormattedDate(longToDate(time));
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String getFormattedDate(LocalDateTime time) {
        return String.format("%s %d, %d", time.getMonth(), time.getDayOfMonth(), time.getYear());
    }

    @Override
    public String getFormattedTime(long time) {
        return getFormattedTime(longToDate(time));
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String getFormattedTime(LocalDateTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }

    @Override
    public String getLocale() {
        return "UTC";
    }
}
