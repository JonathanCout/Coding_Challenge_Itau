package com.example.jonathan_coutinho.CodingChallenge.repository;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByName(String name);
}
