package com.timirlanyat.odb.converters;

import com.timirlanyat.odb.model.AttributeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAttributeTypeConverter implements Converter<String, AttributeType> {

    @Override
    public AttributeType convert(String source) {
        if (source == null || source.length() == 0)
            return null;

        return AttributeType.valueOf(source.toUpperCase());
    }
}