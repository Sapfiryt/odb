package com.timirlanyat.odb.dal.entity;

import com.timirlanyat.odb.model.Attribute;
import com.timirlanyat.odb.model.Reconstruction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "attributes_in_reconstrution")
@Getter
@Setter
@Accessors(chain = true)
@IdClass(AttributeReconstructionPK.class)
public class AttributesInReconstructions implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @Id
    @ManyToOne
    @JoinColumn(name = "reconstruction_id")
    private Reconstruction reconstruction;

    @Column(name = "amount")
    private Integer amountOf;
}
