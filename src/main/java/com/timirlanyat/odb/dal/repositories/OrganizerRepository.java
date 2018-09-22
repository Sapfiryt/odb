package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Organizer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends CrudRepository<Organizer,Integer> {
}
