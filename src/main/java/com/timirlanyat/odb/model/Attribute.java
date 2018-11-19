package com.timirlanyat.odb.model;

import com.timirlanyat.odb.converters.AttributeTypeConverter;
import com.timirlanyat.odb.dal.entity.AttributesInReconstructions;
import com.timirlanyat.odb.dal.entity.AttributesInUse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="attributes")
@Getter
@Setter
@Accessors(chain = true)
public class Attribute {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Convert(converter = AttributeTypeConverter.class)
    private AttributeType type;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "attribute",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<AttributesInUse> members = new HashSet<>();

    @OneToMany(mappedBy = "attribute",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private Set<AttributesInReconstructions> reconstructions= new HashSet<>();

    public Attribute(){}
}
