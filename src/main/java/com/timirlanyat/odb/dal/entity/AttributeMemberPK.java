package com.timirlanyat.odb.dal.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
public class AttributeMemberPK implements Serializable {

    private Integer attribute;

    private Integer member;

    private Integer reconstruction;
}