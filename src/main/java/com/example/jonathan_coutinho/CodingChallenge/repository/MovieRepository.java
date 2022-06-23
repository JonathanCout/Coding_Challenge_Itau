package com.example.jonathan_coutinho.CodingChallenge.repository;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    Movie findByName(String name);
}
