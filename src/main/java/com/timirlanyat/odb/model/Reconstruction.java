package com.timirlanyat.odb.model;

import com.timirlanyat.odb.dal.entity.AttributesInReconstructions;
import com.timirlanyat.odb.dal.entity.AttributesInUse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="reconstructions")
@Getter
@Setter
@Accessors(chain = true)
@Check(constraints = "status in ('open','recruitment closed','in progress','closed')")
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

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Organizer.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @ManyToMany
    private Set<Member> participants = new HashSet<>();

    @OneToMany(mappedBy = "reconstruction",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<AttributesInReconstructions> attributes= new HashSet<>();

    @OneToMany(mappedBy = "reconstruction",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<AttributesInUse> attributesInUse= new HashSet<>();


    public Reconstruction(){}
}
