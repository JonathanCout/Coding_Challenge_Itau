package jonathan_coutinho.CodingChallenge.service;

import jonathan_coutinho.CodingChallenge.domain.Movie;
import jonathan_coutinho.CodingChallenge.domain.Score;
import jonathan_coutinho.CodingChallenge.domain.User;
import jonathan_coutinho.CodingChallenge.dto.ScoreDTO;
import jonathan_coutinho.CodingChallenge.repository.MovieRepository;
import jonathan_coutinho.CodingChallenge.repository.ScoreRepository;
import jonathan_coutinho.CodingChallenge.repository.UserRepository;
import jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final MovieAPIService movieService;

    public Score createScore (ScoreDTO scoreDTO){
        User user = userRepository.findByUsername(scoreDTO.getUsername()).orElseThrow(() ->
                new NotFoundException("Usuario não encontrado"));

        Movie movie = movieRepository.findByImdbid(scoreDTO.getMovieId()).orElse(movieService.getMovieFromAPIWithId(scoreDTO.getMovieId()));

        Score score = new Score(scoreDTO,user,movie);

        movie.updateScore(score);
        movieRepository.save(movie);

        user.pointsHandler(1);
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
