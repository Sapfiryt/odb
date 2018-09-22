package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    @Query(value = "select u from User u where lower(u.email) = lower(:email)")
    User findByEmail(@Param("email") String email)  ;

}
