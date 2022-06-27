package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.service.MovieService;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("movie")
public class MovieController {

    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String id) {
        HttpResponse<JsonNode> response = Unirest.get("http://www.omdbapi.com/?apikey=[yourkey]&")
                .routeParam("yourkey", id.toString())
                .asJson();

        JSONObject jsonObject = response.getBody().getObject();

        Movie newMovie = new Movie(jsonObject.getString("imdbID"), jsonObject.getString("Title"), jsonObject.getString("Year"));
        movieService.createMovie(newMovie);

        return ResponseEntity.ok(movieService.getMovieByImobID(id).get());
    }
}
