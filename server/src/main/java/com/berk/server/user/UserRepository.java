package com.berk.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN " +
            "true ELSE false END " +
            "FROM User u " +
            "WHERE u.name = ?1")
    boolean existsByName(String name);
}