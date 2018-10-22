package com.timirlanyat.odb.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="organizers")
@Getter
@Setter
@Accessors(chain = true)
public class Organizer extends Member {


    @Column(name = "agreement")
    private String numAgreement;

    @Column(name = "approved")
    private Boolean approved;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "organizer"
    )
    private Set<Reconstruction> managedReconstructions= new HashSet<>();

    public Organizer(){}


    public void addReconstruction(Reconstruction reconstruction) {
        managedReconstructions.add(reconstruction);
        reconstruction.setOrganizer(this);
    }

    public void removeReconstruction(Reconstruction reconstruction) {
        managedReconstructions.remove(reconstruction);
        reconstruction.setOrganizer(null);
    }


}
