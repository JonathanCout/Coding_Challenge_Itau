package jonathan_coutinho.CodingChallenge.controller;

import jonathan_coutinho.CodingChallenge.domain.Score;
import jonathan_coutinho.CodingChallenge.dto.ScoreDTO;
import jonathan_coutinho.CodingChallenge.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping
    public ResponseEntity<Score> createScore(@RequestBody ScoreDTO scoreDTO){
        return ResponseEntity.ok(scoreService.createScore(scoreDTO));
    }

    @PutMapping
    public ResponseEntity<Score> updateScore(@RequestBody ScoreDTO scoreDTO){
        return ResponseEntity.ok(scoreService.editScore(scoreDTO));
    }

}
