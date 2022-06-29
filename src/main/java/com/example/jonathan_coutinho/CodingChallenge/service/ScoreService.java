package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.domain.Score;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.dto.ScoreDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.MovieRepository;
import com.example.jonathan_coutinho.CodingChallenge.repository.ScoreRepository;
import com.example.jonathan_coutinho.CodingChallenge.repository.UserRepository;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    public Score createScore (ScoreDTO scoreDTO){
        User user = userRepository.findByUsername(scoreDTO.getUsername()).orElseThrow(() ->
                new NotFoundException("Usuario não encontrado"));

        Movie movie = movieRepository.findByImdbid(scoreDTO.getMovieId()).orElseThrow(() ->
                new NotFoundException("Filme não encontrado"));

        Score score = new Score(scoreDTO,user,movie);

        movie.updateScore(score);
        movieRepository.save(movie);

        user.pointsHandler();
        userRepository.save(user);

        return scoreRepository.save(score);
    }

    public Score editScore (ScoreDTO scoreDTO){
        Score score = scoreRepository.findById(scoreDTO.getId()).orElseThrow(() ->
                new NotFoundException("Avaliação não encontrada"));
        score.setScore(scoreDTO.getScore());

        return scoreRepository.save(score);
    }
}
