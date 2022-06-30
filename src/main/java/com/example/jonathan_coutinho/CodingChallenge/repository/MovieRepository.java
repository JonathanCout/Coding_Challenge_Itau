package com.example.jonathan_coutinho.CodingChallenge.repository;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    Optional<Movie> findByImdbid(String imdbID);
    Optional<Movie> findByTitle(String name);
}
