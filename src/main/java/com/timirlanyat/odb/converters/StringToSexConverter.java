package com.timirlanyat.odb.converters;

import com.timirlanyat.odb.model.Sex;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSexConverter
        implements Converter<String, Sex> {

    @Override
    public Sex convert(String source) {
        if(source==null||source.length()==0)
            return null;

        return Sex.valueOf(source.toUpperCase());
    }
}