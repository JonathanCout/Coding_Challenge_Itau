package jonathan_coutinho.CodingChallenge.dto;

import jonathan_coutinho.CodingChallenge.domain.Comment;
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
    private String comment;
    private LocalDateTime timeNDate;

    public NewCommentDTO(Comment comment){
        this.userId = comment.getUser().getId();
        this.movieId = comment.getMovie().getImdbid();
        this.comment = comment.getComment();
        this.timeNDate = comment.getDateNTime();
    }
}
