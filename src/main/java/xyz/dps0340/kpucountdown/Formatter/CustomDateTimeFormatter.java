package xyz.dps0340.kpucountdown.Formatter;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public abstract class CustomDateTimeFormatter {
    public static final DateTimeFormatter koreanFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .toFormatter();
}
