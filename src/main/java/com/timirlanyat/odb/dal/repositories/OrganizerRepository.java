package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Organizer;
import com.timirlanyat.odb.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizerRepository extends CrudRepository<Organizer,Integer> {


    @Query(value = "select org from Organizer org where org.approved = false or org.approved is null")
    List<Organizer> findNotApproved();

}
