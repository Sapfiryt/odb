package com.timirlanyat.odb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name="hashed")
@Getter
@Setter
@Accessors(chain = true)
public class Hash {


    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @Column(name = "hash")
    private String hash;
}
