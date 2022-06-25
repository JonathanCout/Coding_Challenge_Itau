package com.example.jonathan_coutinho.CodingChallenge.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tb_movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String iMoBid;
    private String title;
    private String year;

    @OneToMany(mappedBy = "movie")
    private List<Comment> commentaries;

    @OneToMany(mappedBy = "movie")
    private List<Score> scores;
    private Float score;
    private Integer counter;

    public Movie(String iMoBid, String title, String year) {
        this.iMoBid = iMoBid;
        this.title = title;
        this.year = year;
    }

    public void updateScore(Score score){
        this.setScore(((this.getScore() * this.getCounter()) + score.getScore())/this.getCounter()+1);
        this.setCounter(this.getCounter()+1);
    }
}
