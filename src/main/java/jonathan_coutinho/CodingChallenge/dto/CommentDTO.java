package jonathan_coutinho.CodingChallenge.dto;

import jonathan_coutinho.CodingChallenge.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private Long id;
    private String username;
    private String movieName;
    private String comment;

    public CommentDTO(Comment comment){
        this.id = comment.getId();
        this.username = comment.getUser().getUsername();
        this.movieName = comment.getMovie().getTitle();
        this.comment = comment.getComment();
    }
}
