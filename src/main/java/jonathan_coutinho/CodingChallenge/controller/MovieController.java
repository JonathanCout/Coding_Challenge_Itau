package jonathan_coutinho.CodingChallenge.controller;

import jonathan_coutinho.CodingChallenge.domain.Comment;
import jonathan_coutinho.CodingChallenge.domain.Movie;
import jonathan_coutinho.CodingChallenge.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String id) {
        return ResponseEntity.ok(movieService.createMovie(id));
    }

    @GetMapping("/title")
    public ResponseEntity<Movie> getMovieByTitle(@RequestParam String title, @RequestParam(required = false) String year) {
        return ResponseEntity.ok(movieService.getMovieByTitle(title,year));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllCommentsById(@RequestParam String id){
        return ResponseEntity.ok(movieService.getAllComments(id));
    }
}
