package jonathan_coutinho.CodingChallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jonathan_coutinho.CodingChallenge.dto.NewCommentDTO;
import jonathan_coutinho.CodingChallenge.dto.ReplyCommentDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties(value = {"commentaries","scores"})
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnoreProperties(value = {"commentaries","scores"})
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private String comment;
    private LocalDateTime dateNTime;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> previousId;
    private Integer reaction;
    private boolean isDuplicate;

    public Comment(NewCommentDTO newCommentDTO, User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        this.comment = newCommentDTO.getComment();
        this.dateNTime = newCommentDTO.getTimeNDate();
    }

    public Comment(ReplyCommentDTO replyCommentDTO, User user, Movie movie){
        this.user = user;
        this.movie = movie;
        this.comment = replyCommentDTO.getComment();
        this.previousId = replyCommentDTO.getPreviousId();
        this.dateNTime = replyCommentDTO.getDateNTime();
    }

}
