package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("movie")
@Api(tags = {"Filmes"})
@Tag(name = "Filmes",description = "Endpoint para controlar buscar de filmes")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @ApiOperation("Encontra um filme usando a id do imdb e adiciona o filme para o banco de dados")
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String id) {
        return ResponseEntity.ok(movieService.createMovie(id));
    }

    @ApiOperation("Encontra um filme usando a id do imdb e adiciona o filme para o banco de dados")
    @GetMapping("/findByTitle")
    public ResponseEntity<Movie> getMovieByTitle(@RequestParam String title, @RequestParam(required = false) String year) {
        return ResponseEntity.ok(movieService.getMovieByTitle(title,year));
    }

    @ApiOperation("Encontrar somente os comentários para um filme específico")
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllCommentsById(@RequestParam String id){
        return ResponseEntity.ok(movieService.getAllComments(id));
    }
}
