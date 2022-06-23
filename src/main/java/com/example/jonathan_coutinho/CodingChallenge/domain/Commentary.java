package com.example.jonathan_coutinho.CodingChallenge.domain;

import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentaryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_commentaries")
public class Commentary {

    private Long id;
    private User user;
    private Movie movie;
    private String commentary;
    private Float score;

    public Commentary(NewCommentaryDTO newCommentaryDTO, User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        this.commentary = newCommentaryDTO.getCommentary();
        this.score = newCommentaryDTO.getScore();
    }

    public Commentary(User user, Movie movie, Float score) {
        this.user = user;
        this.movie = movie;
        this.score = score;
    }

}
