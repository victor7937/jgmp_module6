package com.epam.victor.controller.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    public static String InstantToIsoDate(Instant instantDate) {
        LocalDate date = LocalDate.ofInstant(instantDate, ZoneId.of("UTC"));
        return date.format(DateTimeFormatter.ISO_DATE);
    }
}
