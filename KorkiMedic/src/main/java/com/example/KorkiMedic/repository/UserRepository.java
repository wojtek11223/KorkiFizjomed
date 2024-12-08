package com.example.KorkiMedic.repository;


import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findAll();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role AND u.id <> :currentUserId")
    List<User> findAllByRoleAndNotCurrentUser(@Param("role") Role role, @Param("currentUserId") Long currentUserId);

    Optional<User> findByIdAndRoles(Long doctorId, Role role);
}
