package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Check;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckRepository extends CrudRepository<Check,Integer> {
}
