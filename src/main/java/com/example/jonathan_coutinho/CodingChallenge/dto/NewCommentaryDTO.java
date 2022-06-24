package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.Commentary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewCommentaryDTO {

    private Long userId;
    private Long movieId;
    private String commentary;
    private Float score;


    public NewCommentaryDTO(Commentary commentary){
        this.userId = commentary.getUser().getId();
        this.movieId = commentary.getMovie().getId();
        this.commentary = commentary.getCommentary();
        this.score = commentary.getScore();
    }
}
