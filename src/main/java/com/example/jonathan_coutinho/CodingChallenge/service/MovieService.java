package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
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
    private MovieRepository movieRepository;

    public Movie createMovie(Movie movie){
        validateMovieInfo(movie);
        Optional<Movie> existingMovie = movieRepository.findByImdbid(movie.getImdbid());
        if(existingMovie.isPresent()){
            return existingMovie.get();
        }
        movie.setCounter(0);
        movie.setScore(0.0F);
        return movieRepository.save(movie);
    }
    public Optional<Movie> getMovieByImdbID(String imodID){
        Optional<Movie> result = movieRepository.findByImdbid(imodID);
        if(result.isEmpty()) throw new BadRequestException("Nenhum filme foi encontrado com o código fornecido");
        return movieRepository.findByImdbid(imodID);
    }

    public List<Movie> getMovieByName(String name){
        List<Movie> result = movieRepository.findByTitle(name);
        if(result.isEmpty()) throw new NotFoundException("Não existem filmes com este nome");
        return result;
    }

    public List<Comment> getAllComments(String imodID){
        Optional<Movie> result = movieRepository.findByImdbid(imodID);
        if(result.isEmpty()) throw new BadRequestException("Nenhum filme foi encontrado com o código fornecido");
        return result.get().getCommentaries();
    }

    public void validateMovieInfo(Movie movie){
        if(movie.getImdbid().isBlank() || movie.getImdbid() == null ||
                movie.getTitle().isBlank() || movie.getTitle() == null ||
                movie.getYear().isBlank() || movie.getYear() == null)
            throw new BadRequestException("Existem dados faltantes do filme");
    }
}
