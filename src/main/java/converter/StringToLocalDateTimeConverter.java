package converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import globals.PizzaServiceConstants;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String localDateTimeAsString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PizzaServiceConstants.DATE_TIME_FORMAT);

        return LocalDateTime.parse(localDateTimeAsString, formatter);
    }

}