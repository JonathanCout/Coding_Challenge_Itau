package jonathan_coutinho.CodingChallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jonathan_coutinho.CodingChallenge.dto.ScoreDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_scores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float score;

    @JsonIgnoreProperties(value = {"commentaries","scores"})
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnoreProperties(value = {"commentaries","scores"})
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public Score(ScoreDTO scoreDTO, User user, Movie movie){
        this.id = scoreDTO.getId();
        this.score = scoreDTO.getScore();
        this.user = user;
        this.movie = movie;
    }
}
