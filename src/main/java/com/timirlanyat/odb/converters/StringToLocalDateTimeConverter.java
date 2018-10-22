package com.timirlanyat.odb.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateTimeConverter
        implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        if(source==null||source.length()==0)
            return null;
        return LocalDateTime.parse(
                source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
