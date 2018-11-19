package com.timirlanyat.odb.model;

import com.timirlanyat.odb.annotation.PasswordMatches;
import com.timirlanyat.odb.annotation.ValidEmail;
import com.timirlanyat.odb.annotation.ValidPhone;
import com.timirlanyat.odb.converters.SexConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@PasswordMatches
public class UserDto {
    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 20)
    private String password;
    @NotNull
    @NotEmpty
    private String matchingPassword;

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 13)
    @ValidPhone
    private String phoneNumber;

    private Sex sex;

    @NotNull
    private LocalDate dateOfBirth;

    private Boolean organizer;

    private String agreement;
}
