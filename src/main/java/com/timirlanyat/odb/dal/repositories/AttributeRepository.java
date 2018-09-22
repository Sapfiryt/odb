package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Attribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends CrudRepository<Attribute,Integer> {
}
