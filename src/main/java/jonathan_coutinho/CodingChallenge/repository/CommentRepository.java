package jonathan_coutinho.CodingChallenge.repository;

import jonathan_coutinho.CodingChallenge.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
