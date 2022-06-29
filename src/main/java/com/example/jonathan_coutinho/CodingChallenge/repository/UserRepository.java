package com.example.jonathan_coutinho.CodingChallenge.repository;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);

    @Query("UPDATE User u SET u.enabled = ?2  WHERE u.username = ?1")
    void updateEnableStatus(@Param("username") String username, @Param("enabled") boolean enabled);

    @Query("UPDATE User u SET u.failedAttempts = ?2  WHERE u.username = ?1")
    void updateFailedAttempts(@Param("username") String username, @Param("failedAttempts") int failedAttempts);

}
