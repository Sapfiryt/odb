package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Organizer;
import com.timirlanyat.odb.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends CrudRepository<Organizer,Integer> {

    @Query(value = "select org from Organizer org where org.user = :user")
    Organizer findByUser(@Param("user") User user)  ;
}
