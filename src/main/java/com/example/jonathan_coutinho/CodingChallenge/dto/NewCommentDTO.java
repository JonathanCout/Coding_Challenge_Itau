package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NewCommentDTO {

    private Long userId;
    private String movieId;
    private String commentary;
    private LocalDateTime timeNDate;

    public NewCommentDTO(Comment comment){
        this.userId = comment.getUser().getId();
        this.movieId = comment.getMovie().getIMoBid();
        this.commentary = comment.getComment();
        this.timeNDate = comment.getDateNTime();
    }
}
