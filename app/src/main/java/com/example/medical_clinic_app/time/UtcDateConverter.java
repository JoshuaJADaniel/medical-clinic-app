package com.example.medical_clinic_app.time;

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
}
