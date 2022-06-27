package com.example.jonathan_coutinho.CodingChallenge.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tb_movies")
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imdbid;
    private String title;
    private String year;

    @OneToMany(mappedBy = "movie")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> commentaries;

    @OneToMany(mappedBy = "movie")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Score> scores;
    private Float score;
    private Integer counter;

    public Movie(String imdbid, String title, String year) {
        this.imdbid = imdbid;
        this.title = title;
        this.year = year;
    }

    public void updateScore(Score score){
        this.setScore(((this.getScore() * this.getCounter()) + score.getScore())/(this.getCounter()+1));
        this.setCounter(this.getCounter()+1);
    }
}
