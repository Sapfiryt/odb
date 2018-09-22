package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Hash;
import com.timirlanyat.odb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashRepository  extends CrudRepository<Hash,Integer> {
}
