package com.timirlanyat.odb.model;


import com.timirlanyat.odb.annotation.ParticipantsLimit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@ParticipantsLimit
public class ReconstructionDto {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private LocalDate year;

    @NotNull
    private LocalDateTime dateOf;

    @NotNull
    @Positive
    private Float cost;

    @NotNull
    @NotEmpty
    private String about;

    @NotNull
    @Positive
    private Integer minParticipants;

    @NotNull
    @Positive
    private Integer maxParticipants;

    @NotNull
    private Integer locationId;


    private List<Integer> attributesId;

    private List<Integer> amount;


}
