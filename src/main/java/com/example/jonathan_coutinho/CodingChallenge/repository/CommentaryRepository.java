package com.example.jonathan_coutinho.CodingChallenge.repository;

import com.example.jonathan_coutinho.CodingChallenge.domain.Commentary;
import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long> {

    List<Commentary> findAllByUser(User user);
    List<Commentary> findAllByMovie(Movie movie);
}
