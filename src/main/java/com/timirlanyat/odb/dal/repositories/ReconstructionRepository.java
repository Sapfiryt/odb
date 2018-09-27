package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Reconstruction;
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
}
