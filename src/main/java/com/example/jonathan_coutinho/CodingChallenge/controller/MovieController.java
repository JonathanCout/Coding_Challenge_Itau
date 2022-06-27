package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("movie")
@Api(tags = {"Filmes"})
@Tag(name = "Filmes",description = "Endpoint para controlar buscar de filmes")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String id) {
        HttpResponse<JsonNode> response = Unirest.get("http://www.omdbapi.com/?apikey=c9cee5da&i={movieId}")
                .routeParam("movieId", id.toString())
                .asJson();

        JSONObject jsonObject = response.getBody().getObject();

        Movie newMovie = new Movie(jsonObject.getString("imdbID"), jsonObject.getString("Title"), jsonObject.getString("Year"));
        movieService.createMovie(newMovie);

        return ResponseEntity.ok(movieService.getMovieByImdbID(id).get());
    }
}
