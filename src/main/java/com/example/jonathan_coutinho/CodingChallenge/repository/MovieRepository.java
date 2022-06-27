package com.example.jonathan_coutinho.CodingChallenge.repository;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    Optional<Movie> findByImobid(String imobID);
    List<Movie> findByTitle(String name);
}
