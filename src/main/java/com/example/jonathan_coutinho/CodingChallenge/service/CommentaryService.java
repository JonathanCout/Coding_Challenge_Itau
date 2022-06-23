package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Commentary;
import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.dto.CommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.CommentaryRepository;
import com.example.jonathan_coutinho.CodingChallenge.repository.MovieRepository;
import com.example.jonathan_coutinho.CodingChallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentaryService {

    @Autowired
    private CommentaryRepository commentaryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    public Commentary createCommentary(NewCommentaryDTO newCommentaryDTO) throws Exception {
        User user = userRepository.findById(newCommentaryDTO.getUserId()).orElseThrow(RuntimeException::new);
        Movie movie = movieRepository.findById(newCommentaryDTO.getMovieId()).orElseThrow(RuntimeException::new);

        if(newCommentaryDTO.getCommentary().isEmpty()){
            return commentaryRepository.save(new Commentary(user,movie, newCommentaryDTO.getScore()));
        }
        return commentaryRepository.save(new Commentary(newCommentaryDTO,user,movie));
    }

    public List<CommentaryDTO> getCommentariesByUser(Long id){
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Commentary> commentaries = commentaryRepository.findAllByUser(user);

        if(commentaries.isEmpty()){
            throw new RuntimeException();
        }
        return commentaries.stream().map(CommentaryDTO::new).collect(Collectors.toList());
    }

    public List<CommentaryDTO> getCommentariesByMovie(Long id){
        Movie movie = movieRepository.findById(id).orElseThrow();
        List<Commentary> commentaries = commentaryRepository.findAllByMovie(movie);

        if(commentaries.isEmpty()){
            throw new RuntimeException();
        }
        return commentaries.stream().map(CommentaryDTO::new).collect(Collectors.toList());
    }

    public void deleteCommentary(Long id){ commentaryRepository.deleteById(id); }
}
