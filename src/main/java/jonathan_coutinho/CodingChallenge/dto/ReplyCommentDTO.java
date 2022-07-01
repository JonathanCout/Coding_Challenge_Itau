package jonathan_coutinho.CodingChallenge.dto;

import jonathan_coutinho.CodingChallenge.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentDTO {

    private List<Long> previousId;
    private Long userId;
    private String movieId;
    private String comment;
    private LocalDateTime dateNTime;

    public ReplyCommentDTO(Comment comment, List<Long> previousId){
        this.previousId = previousId;
        this.userId = comment.getUser().getId();
        this.movieId = comment.getMovie().getImdbid();
        this.comment = comment.getComment();
        this.dateNTime = comment.getDateNTime();
    }

    public ReplyCommentDTO(NewCommentDTO newCommentDTO, List<Long> previousId){
        this.previousId = previousId;
        this.userId = newCommentDTO.getUserId();
        this.movieId = newCommentDTO.getMovieId();
        this.comment = newCommentDTO.getComment();
        this.dateNTime = newCommentDTO.getTimeNDate();
    }
}
