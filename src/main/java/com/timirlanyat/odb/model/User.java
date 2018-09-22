package com.timirlanyat.odb.model;

import com.timirlanyat.odb.annotation.ValidEmail;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@Accessors(chain = true)
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;


    @Column(name="email",unique = true)
    private String email;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="phone_number")
    private String phoneNumber;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy="participants"
    )
    private Set<Reconstruction> reconstructions = new HashSet<>();

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy="users"
    )
    private Set<Attribute> attributes = new HashSet<>();

    @Transient
    private List<String> roles = new ArrayList<>();


    public User(){}
}
