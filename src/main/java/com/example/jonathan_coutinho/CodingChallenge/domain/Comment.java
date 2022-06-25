package com.example.jonathan_coutinho.CodingChallenge.domain;

import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ReplyCommentDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private String comment;
    private Long previousId;
    private Integer reaction;
    private boolean isDuplicate;

    public Comment(NewCommentDTO newCommentDTO, User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        this.comment = newCommentDTO.getCommentary();
        user.pointsHandler();
    }

    public Comment(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        user.pointsHandler();
    }

    public Comment(ReplyCommentDTO replyCommentDTO, User user, Movie movie){
        this.user = user;
        this.movie = movie;
        this.comment = replyCommentDTO.getCommentary();
        this.previousId = replyCommentDTO.getPreviousId();
        user.pointsHandler();
    }

}
