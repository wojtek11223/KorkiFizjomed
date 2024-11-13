package com.example.KorkiMedic.repository;


import com.example.KorkiMedic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByFirstName(String name);
    User findByEmailOrPhoneNumber(String email, String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findAll();

}
