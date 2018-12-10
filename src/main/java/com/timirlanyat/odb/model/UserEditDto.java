package com.timirlanyat.odb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;



@Getter
@Setter
@ToString
public class UserEditDto {


    private String firstName;


    private String lastName;
}
