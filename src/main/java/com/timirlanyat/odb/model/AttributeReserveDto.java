package com.timirlanyat.odb.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class AttributeReserveDto {

    @NotNull
    private List<Integer> attributesId;

    @NotNull
    private List<Integer> amount;

    @NotNull
    private Integer reconstructionId;


}
