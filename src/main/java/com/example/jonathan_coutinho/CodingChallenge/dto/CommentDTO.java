package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private Long id;
    private String userName;
    private String movieName;
    private String comment;

    public CommentDTO(Comment comment){
        this.id = comment.getId();
        this.userName = comment.getUser().getUserName();
        this.movieName = comment.getMovie().getTitle();
        this.comment = comment.getComment();
    }
}
