package com.timirlanyat.odb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
public class AttributeDto {

    @NotNull
    @Positive
    Integer amount;

    @NotNull
    @Positive
    Float cost;

    @NotNull
    @NotEmpty
    String description;

    @NotNull
    @NotEmpty
    String name;

    @NotNull
    AttributeType type;
}
