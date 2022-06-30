package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MovieAPIService {

    public Movie getMovieFromAPIWithId(String id){
        HttpResponse<JsonNode> response = Unirest.get("http://www.omdbapi.com/?apikey=c9cee5da&i={movieId}")
                .routeParam("movieId", id)
                .asJson();

        JSONObject jsonObject = response.getBody().getObject();

        return new Movie(jsonObject.getString("imdbID"), jsonObject.getString("Title"), jsonObject.getString("Year"));
    }

    public Movie getMovieFromAPIWithTitle(String title,String year){
        HttpResponse<JsonNode> response = Unirest.get("http://www.omdbapi.com/?apikey=c9cee5da&t={movieTitle}&y={movieYear}")
                .routeParam("movieId", title)
                .routeParam("movieYear", year)
                .asJson();

        JSONObject jsonObject = response.getBody().getObject();

        return new Movie(jsonObject.getString("imdbID"), jsonObject.getString("Title"), jsonObject.getString("Year"));
    }
}
