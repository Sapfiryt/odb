package com.timirlanyat.odb.annotation;

import com.timirlanyat.odb.util.validators.ParticipantsLimitValodator;
import com.timirlanyat.odb.util.validators.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ParticipantsLimitValodator.class)
@Documented
public @interface  ParticipantsLimit {
    String message() default "Value must be bigger then min participants";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
