package com.timirlanyat.odb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="checks")
@Getter
@Setter
@Accessors(chain = true)
public class Check {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "checking_account")
    private String checkingAccount;

    @Column(name = "total")
    private Float total;

    @ManyToOne(cascade =
            {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name="reconstruction_id")
    private Reconstruction reconstruction;

    @ManyToOne(cascade =
            {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name="user_id")
    private Member member;

    public Check(){}
}

