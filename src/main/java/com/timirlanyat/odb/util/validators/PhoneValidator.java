package com.timirlanyat.odb.util.validators;

import com.timirlanyat.odb.annotation.ValidEmail;
import com.timirlanyat.odb.annotation.ValidPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private Pattern pattern;
    private Matcher matcher;
    private static final String PHONE_PATTERN = "^\\+((380)|(995)|(375)|(7))\\d+$";

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context){
        return (validatePhone(phone));
    }

    private boolean validatePhone(String phone) {
        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }

}