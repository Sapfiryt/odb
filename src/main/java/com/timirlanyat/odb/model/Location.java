package com.timirlanyat.odb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.sql.LobTypeMappings;

import javax.persistence.*;

@Entity
@Table(name="locations")
@Getter
@Setter
@Accessors(chain = true)
public class Location {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "photo")
    private byte[] photo;

    @Transient
    private String img;


    public Location(){}


}
