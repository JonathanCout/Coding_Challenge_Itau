package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewCommentDTO {

    private Long userId;
    private String movieId;
    private String commentary;


    public NewCommentDTO(Comment comment){
        this.userId = comment.getUser().getId();
        this.movieId = comment.getMovie().getIMoBid();
        this.commentary = comment.getComment();
    }
}
