package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.Score;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScoreDTO {

    private Long id;
    private Float score;
    private String userName;
    private String movieId;

    public ScoreDTO(Score score){
        this.id = score.getId();
        this.score = score.getScore();
        this.userName = score.getUser().getUserName();
        this.movieId = score.getMovie().getIMoBid();
    }
}
