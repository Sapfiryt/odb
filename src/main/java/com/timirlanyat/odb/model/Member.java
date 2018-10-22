package com.timirlanyat.odb.model;


import com.timirlanyat.odb.converters.SexConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="members")
@Getter
@Setter
@Accessors(chain = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Check(constraints = "sex in ('Male','Female','Not specified'),role in ('REC_MEMBER','REC_ORG','ADMIN')")

public class Member implements User {

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

    @Column(name="phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "sex")
    @Convert(converter = SexConverter.class)
    private Sex sex;

    @Column(name = "dob")
    private LocalDate dateOfBirth;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "participants"
    )
    private Set<Reconstruction> reconstructions = new HashSet<>();

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy="members"
    )
    private Set<Attribute> attributes = new HashSet<>();

    @Column(name = "admin")
    private Boolean admin;

    @Column(name = "role")
    @ElementCollection
    private List<String> roles = new ArrayList<>();

    public Member(){}

    @Override
    public List<String> getRoles(){
        if(admin&&!roles.contains("ADMIN"))
            roles.add("ADMIN");
        return roles;
    }
}
