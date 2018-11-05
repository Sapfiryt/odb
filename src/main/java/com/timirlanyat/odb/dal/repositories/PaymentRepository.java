package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Member;
import com.timirlanyat.odb.model.Payment;
import com.timirlanyat.odb.model.Reconstruction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PaymentRepository extends CrudRepository<Payment,Integer> {

    @Query(value = "select payment from Payment payment " +
            "where payment.member=:member and payment.reconstruction = :reconstruction")
    Iterable<Payment> findAllByReconstructionAndMember(@Param("member") Member member,
                                                       @Param("reconstruction")Reconstruction reconstruction);
    @Query(value = "select payment from Payment payment " +
            "where payment.member=:member")
    Iterable<Payment> findAllByMember(@Param("member") Member member);

    @Query(value = "select payment from Payment payment " +
            "where payment.reconstruction = :reconstruction")
    Iterable<Payment> findAllByReconstruction(@Param("reconstruction")Reconstruction reconstruction);
}
