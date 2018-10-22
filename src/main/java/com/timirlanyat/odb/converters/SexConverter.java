package com.timirlanyat.odb.converters;

import com.timirlanyat.odb.model.Sex;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SexConverter implements AttributeConverter<Sex, String> {

    public String convertToDatabaseColumn(Sex value) {
        if ( value == null )
            return null;

        if(value.name().equals(Sex.NOT_SPECIFIED.name()))
            return "Not specified";

        return StringUtils.capitalize(value.name().toLowerCase());
    }

    public Sex convertToEntityAttribute(String value) {
        if ( value == null || value.length()==0)
            return null;

        if(value.equals("Not specified"))
            return Sex.NOT_SPECIFIED;


        return Sex.valueOf(value.toUpperCase());
    }
}
