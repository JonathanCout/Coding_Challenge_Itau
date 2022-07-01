package jonathan_coutinho.CodingChallenge.repository;

import jonathan_coutinho.CodingChallenge.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Long> {
}
