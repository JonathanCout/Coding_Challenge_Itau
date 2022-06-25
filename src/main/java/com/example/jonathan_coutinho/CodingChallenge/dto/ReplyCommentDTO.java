package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentDTO {

    private Long previousId;
    private Long userId;
    private String movieId;
    private String commentary;

    public ReplyCommentDTO(Comment comment, Long previousId){
        this.previousId = previousId;
        this.userId = comment.getUser().getId();
        this.movieId = comment.getMovie().getIMoBid();
        this.commentary = comment.getComment();
    }
}
