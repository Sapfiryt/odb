package com.timirlanyat.odb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
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
    private String type;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "about")
    private Integer about;

    @ManyToMany
    private Set<User> users = new HashSet<>();

    public Attribute(){}
}
