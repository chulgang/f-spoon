package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime parseToDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static String parseToString(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
