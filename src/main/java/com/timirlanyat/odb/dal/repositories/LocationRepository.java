package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location,Integer> {
}
