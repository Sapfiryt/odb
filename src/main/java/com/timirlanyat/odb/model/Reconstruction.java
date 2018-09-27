package com.timirlanyat.odb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="reconstructions")
@Getter
@Setter
@Accessors(chain = true)
public class Reconstruction {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private LocalDate year;

    @Column(name = "date_of")
    private LocalDateTime dateOf;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "status")
    private String status;

    @Column(name = "about")
    private String about;

    @Column(name = "min_participant")
    private Integer minParticipant;

    @Column(name = "max_participant")
    private Integer maxParticipant;

    @ManyToOne(cascade =
            {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name="location_id")
    private Location location;

    @ManyToMany
    private Set<User> participants = new HashSet<>();


    public Reconstruction(){}
}
