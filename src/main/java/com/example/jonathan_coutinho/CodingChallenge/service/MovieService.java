package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.repository.MovieRepository;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.BadRequestException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    public Movie createMovie(Movie movie){
        validateMovieInfo(movie);
        Optional<Movie> existingMovie = repository.findByImdbID(movie.getIMoBid());
        if(existingMovie.isPresent()) throw new BadRequestException("O filme já está cadastrado na nossa base");
        return repository.save(movie);
    }
    public Optional<Movie> getMovieByImobID(String imobID){
        Optional<Movie> result = repository.findByImdbID(imobID);
        if(result.isEmpty()) throw new BadRequestException("Nenhum filme foi encontrado com o código fornecido");
        return repository.findByImdbID(imobID);
    }
    public List<Movie> getMovieByName(String name){
        List<Movie> result = repository.findByName(name);
        if(result.isEmpty()) throw new NotFoundException("Não existem filmes com este nome");
        return result;
    }

    public void validateMovieInfo(Movie movie){
        if(movie.getIMoBid().isBlank() || movie.getIMoBid() == null ||
                movie.getTitle().isBlank() || movie.getTitle() == null ||
                movie.getYear().isBlank() || movie.getYear() == null)
            throw new BadRequestException("Existem dados faltantes do filme");
    }
}
