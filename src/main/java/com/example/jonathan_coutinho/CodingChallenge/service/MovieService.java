package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    public Movie createMovie(Movie movie){
        return repository.save(movie);
    }
    public Optional<Movie> getMovieById(Long id){
        return repository.findById(id);
    }
    public Movie getMovieByName(String name){
        return repository.findByName(name);
    }
    public void deleteMovie(Long id){
        repository.deleteById(id);
    }
}
