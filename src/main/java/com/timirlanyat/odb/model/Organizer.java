package com.timirlanyat.odb.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="organizers")
@Getter
@Setter
@Accessors(chain = true)
public class Organizer {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column(name = "agreement")
    private String numAgreement;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(
            cascade =
            {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    private Set<Reconstruction> reconstructions= new HashSet<Reconstruction>();

    public Organizer(){}

    public Organizer(User user){
        this.user=user;
        this.setEmail(user.getEmail());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setPhoneNumber(user.getPhoneNumber());
    }



    public Organizer setEmail(String email){
        this.user.setEmail(email);
        return this;
    }

    public Organizer setFirstName(String firstName){
        this.user.setFirstName(firstName);
        return this;

    }

    public Organizer setLastName(String lastName){
        this.user.setLastName(lastName);
        return this;
    }

    public Organizer setPhoneNumber(String phoneNumber){
        this.user.setPhoneNumber(phoneNumber);
        return this;
    }

    public Organizer addReconstruction(Reconstruction rec){
        this.reconstructions.add(rec);
        return this;
    }


    public String getPhoneNumber(){
        return this.user.getPhoneNumber();
    }

    public String getFirstName(){
        return this.user.getFirstName();
    }

    public String getLastName(){
        return this.user.getLastName();
    }

    public String getEmail(){
        return this.user.getEmail();
    }
}
