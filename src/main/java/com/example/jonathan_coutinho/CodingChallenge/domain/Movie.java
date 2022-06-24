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
    private Long id;
    private String iMoBid;
    private String title;
    private String year;
    private float score;
    @OneToMany(mappedBy = "movie")
    private List<Commentary> commentaries;
    private int counter;

    public Movie(String iMoBid, String title, String year) {
        this.iMoBid = iMoBid;
        this.title = title;
        this.year = year;
    }

    public void updateScore(Commentary commentary){
        this.setScore(((this.getScore() * this.counter) + commentary.getScore())/(this.getCounter()+1));
        this.setCounter(this.getCounter() + 1);
    }
}
