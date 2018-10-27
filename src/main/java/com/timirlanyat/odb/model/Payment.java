package com.timirlanyat.odb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="payment")
@Getter
@Setter
@Accessors(chain = true)
public class Payment {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name="date_of")
    private LocalDateTime dateOf;

    @Column(name = "settlement_number")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    private UUID settlementNumber;

    @OneToOne
    private Reconstruction reconstruction;

    @OneToOne
    private Member member;

    @Column(name = "total")
    private Float total;

    public Payment(){

    }
}
