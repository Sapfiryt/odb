package com.timirlanyat.odb.dal.repositories;

import com.timirlanyat.odb.model.Member;
import com.timirlanyat.odb.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member,Integer> {

    @Query(value = "select u from Member u where lower(u.email) = lower(:email)")
    Member findByEmail(@Param("email") String email);

}
