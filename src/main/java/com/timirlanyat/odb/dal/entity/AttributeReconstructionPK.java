package com.timirlanyat.odb.dal.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
public class AttributeReconstructionPK implements Serializable {

    private Integer attribute;

    private Integer reconstruction;
}
