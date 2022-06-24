package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.Commentary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentaryDTO {

    private Long previousId;
    private Long userId;
    private Long movieId;
    private String commentary;

    public ReplyCommentaryDTO(Commentary commentary, Long previousId){
        this.previousId = previousId;
        this.userId = commentary.getUser().getId();
        this.movieId = commentary.getMovie().getId();
        this.commentary = commentary.getCommentary();
    }
}
