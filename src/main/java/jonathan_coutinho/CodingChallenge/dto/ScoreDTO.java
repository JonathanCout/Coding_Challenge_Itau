package jonathan_coutinho.CodingChallenge.dto;

import jonathan_coutinho.CodingChallenge.domain.Score;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScoreDTO {

    private Long id;
    private Float score;
    private String username;
    private String movieId;

    public ScoreDTO(Score score){
        this.id = score.getId();
        this.score = score.getScore();
        this.username = score.getUser().getUsername();
        this.movieId = score.getMovie().getImdbid();
    }
}
