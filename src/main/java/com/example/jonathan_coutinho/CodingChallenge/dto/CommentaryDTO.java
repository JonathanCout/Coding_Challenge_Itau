package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.Commentary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentaryDTO {

    private String userName;
    private String movieName;
    private String commentary;
    private Float score;

    public CommentaryDTO(Commentary commentary){
        this.userName = commentary.getUser().getUserName();
        this.movieName = commentary.getMovie().getName();
        this.commentary = commentary.getCommentary();
        this.score = commentary.getScore();
    }
}
