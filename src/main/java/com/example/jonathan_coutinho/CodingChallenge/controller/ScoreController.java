package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Score;
import com.example.jonathan_coutinho.CodingChallenge.dto.ScoreDTO;
import com.example.jonathan_coutinho.CodingChallenge.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public ResponseEntity<Score> createScore(@RequestBody ScoreDTO scoreDTO){
        return ResponseEntity.ok(scoreService.createScore(scoreDTO));
    }

    @PutMapping
    public ResponseEntity<Score> updateScore(@RequestBody ScoreDTO scoreDTO){
        return ResponseEntity.ok(scoreService.editScore(scoreDTO));
    }

}
