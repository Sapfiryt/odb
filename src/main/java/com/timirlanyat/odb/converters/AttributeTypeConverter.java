package com.timirlanyat.odb.converters;

import com.timirlanyat.odb.model.AttributeType;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AttributeTypeConverter implements AttributeConverter<AttributeType, String> {

    public String convertToDatabaseColumn(AttributeType value) {
        if ( value == null )
            return null;

        return StringUtils.capitalize(value.name().toLowerCase());
    }

    public AttributeType convertToEntityAttribute(String value) {
        if ( value == null || value.length()==0)
            return null;


        return AttributeType.valueOf(value.toUpperCase());
    }
}
