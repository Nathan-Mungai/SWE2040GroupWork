package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A utility class for handling and formatting dates and times.
 */
public class DateUtil {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * Formats a LocalDateTime object into a standard string representation.
     * @param dateTime The LocalDateTime object to format.
     * @return A formatted string, or "N/A" if the input is null.
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    /**
     * Formats a LocalDateTime object and wraps it in a JavaFX StringProperty for TableView compatibility.
     * @param dateTime The LocalDateTime object to format.
     * @return A SimpleStringProperty containing the formatted date-time string.
     */
    public static StringProperty formatDateTimeProperty(LocalDateTime dateTime) {
        return new SimpleStringProperty(format(dateTime));
    }
}
