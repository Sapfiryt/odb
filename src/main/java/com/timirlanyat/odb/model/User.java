package com.timirlanyat.odb.model;

import java.util.List;

public interface User {
    List<String> getRoles();

    User setId(Integer id);

    User setEmail(String email);

    User setFirstName(String firstName);

    User setLastName(String lastName);

    User setPhoneNumber(String phoneNumber);

    User setSex(Sex sex);

    User setDateOfBirth(java.time.LocalDate dateOfBirth);

    User setReconstructions(java.util.Set<Reconstruction> reconstructions);

    User setAttributes(java.util.Set<Attribute> attributes);

    User setAdmin(Boolean admin);

    User setRoles(List<String> roles);

    Integer getId();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getPhoneNumber();

    Sex getSex();

    java.time.LocalDate getDateOfBirth();

    java.util.Set<Reconstruction> getReconstructions();

    java.util.Set<Attribute> getAttributes();

    Boolean getAdmin();
}
