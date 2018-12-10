package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Member;
import com.timirlanyat.odb.model.Reconstruction;
import com.timirlanyat.odb.dal.entity.AttributesInReconstructions;
import com.timirlanyat.odb.dal.entity.AttributesInUse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReconstructionRepository extends CrudRepository<Reconstruction,Integer> {

    @Query(value = "select * from (select * from reconstructions rr " +
            "where (select count(rp.reconstructions_id) from reconstructions_participants rp where rr.id=rp.reconstructions_id) < rr.max_participant " +
            "or(TRUE and not :notFull)) " +
            "r where r.cost <= :cost and r.date_of <= :befor and r.date_of >= :after",
            nativeQuery = true)
    Iterable<Reconstruction> findAllByParams(@Param("cost") Long cost,
                                            @Param("after") LocalDate after,
                                            @Param("befor") LocalDate befor,
                                            @Param("notFull") Boolean notFull);

    @Query(value = "select rec from Reconstruction rec inner join rec.participants partis where partis = :user")
    Iterable<Reconstruction> findByUser(@Param("user")Member user);

    @Modifying
    @Query(value = "UPDATE attributes SET amount = amount+ :amount WHERE attributes.id= :attr_id ;" +
            "DELETE FROM attributes_in_reconstrution WHERE attributes_in_reconstrution.reconstruction_id = :rec_id ;",
            nativeQuery = true)
    void returnAttributes(@Param("amount")Integer amount, @Param("rec_id")Integer rec_id, @Param("attr_id")Integer attr_id);

    @Query(value = "select sum(res.avg) from ((select r.*, avg(a.cost)\n" +
            "                           from reconstructions r\n" +
            "                                  join attributes_in_use amr on r.id = amr.reconstruction_id\n" +
            "                                  join attributes a on amr.attribute_id = a.id\n" +
            "                           group by r.id))as res\n" +
            "where res.id = :rec_id ;", nativeQuery = true)
    Integer averageAttributesCost(@Param("rec_id")Integer rec_id);


    @Query(value = "select sum(p.total) from organizers o\n" +
            "                                 join\n" +
            "                             reconstructions r on o.id = r.organizer_id join payment p on r.id = p.reconstruction_id\n" +
            "where o.id = :org_id and r.id= :rec_id ;", nativeQuery = true)
    Integer totalProfit(@Param("org_id")Integer org_id, @Param("rec_id")Integer rec_id);
}
