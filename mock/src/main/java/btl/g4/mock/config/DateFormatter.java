package btl.g4.mock.config;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

public class DateFormatter implements Formatter<LocalDate> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}

