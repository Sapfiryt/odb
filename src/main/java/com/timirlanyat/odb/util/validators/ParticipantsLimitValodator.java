package com.timirlanyat.odb.util.validators;

import com.timirlanyat.odb.annotation.ParticipantsLimit;
import com.timirlanyat.odb.model.ReconstructionDto;
import com.timirlanyat.odb.model.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ParticipantsLimitValodator
        implements ConstraintValidator<ParticipantsLimit, Object> {

    @Override
    public void initialize(ParticipantsLimit constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        ReconstructionDto dto = (ReconstructionDto) obj;
        return dto.getMaxParticipants()>=dto.getMinParticipants();
    }
}
