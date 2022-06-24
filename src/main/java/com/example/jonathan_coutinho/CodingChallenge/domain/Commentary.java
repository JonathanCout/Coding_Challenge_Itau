package com.example.jonathan_coutinho.CodingChallenge.domain;

import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ReplyCommentaryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_commentaries")
public class Commentary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private String commentary;
    private Float score;
    private Long previousId;
    private Integer reaction;

    public Commentary(NewCommentaryDTO newCommentaryDTO, User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        this.commentary = newCommentaryDTO.getCommentary();
        this.score = newCommentaryDTO.getScore();
        user.pointsHandler();
    }

    public Commentary(User user, Movie movie, Float score) {
        this.user = user;
        this.movie = movie;
        this.score = score;
        user.pointsHandler();
    }

    public Commentary(ReplyCommentaryDTO replyCommentaryDTO, User user, Movie movie){
        this.user = user;
        this.movie = movie;
        this.commentary = replyCommentaryDTO.getCommentary();
        this.previousId = replyCommentaryDTO.getPreviousId();
        user.pointsHandler();
    }

}
