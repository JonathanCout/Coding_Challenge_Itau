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
    private String name;
    private String year;
    private float score;
    private List<Commentary> commentaries;
    private int counter;

    public Movie(Long id, String name, String year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public void updateScore(Commentary commentary){
        this.setScore(((this.getScore() * this.counter) + commentary.getScore())/(this.getCounter()+1));
        this.setCounter(this.getCounter() + 1);
    }
}
