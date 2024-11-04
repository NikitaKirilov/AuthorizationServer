package org.example.backend.utils;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;

@UtilityClass
public class TimestampUtils {

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
