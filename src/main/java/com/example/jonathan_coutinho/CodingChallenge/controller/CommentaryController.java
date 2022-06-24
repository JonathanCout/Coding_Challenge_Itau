package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Commentary;
import com.example.jonathan_coutinho.CodingChallenge.dto.CommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ReplyCommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.service.CommentaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RequestMapping("/comment")
public class CommentaryController {

    @Autowired
    private CommentaryService commentaryService;

    @PostMapping
    public ResponseEntity<Commentary> createComment(@RequestBody NewCommentaryDTO newCommentaryDTO){
        return ResponseEntity.ok(commentaryService.createCommentary(newCommentaryDTO));
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<Commentary> replyComment(@RequestBody NewCommentaryDTO newCommentaryDTO, @PathVariable Long id){
        ReplyCommentaryDTO replyCommentaryDTO = new ReplyCommentaryDTO(id, newCommentaryDTO.getUserId(), newCommentaryDTO.getMovieId(), newCommentaryDTO.getCommentary());
        return ResponseEntity.ok(commentaryService.replyCommentary(replyCommentaryDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentaryDTO>> getComment(@PathVariable Long id){
        return ResponseEntity.ok(commentaryService.getComment(id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CommentaryDTO>> getCommentsByUser(@PathVariable Long id){
        return ResponseEntity.ok(commentaryService.getCommentariesByUser(id));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<List<CommentaryDTO>> getCommentsByMovie(@PathVariable Long id){
        return ResponseEntity.ok(commentaryService.getCommentariesByMovie(id));
    }

    @PatchMapping("/{id}/{userName}{reaction}")
    public ResponseEntity<Commentary> patchReaction(@PathVariable Long id,@PathVariable String userName, @PathVariable Integer reaction){
        return ResponseEntity.ok(commentaryService.updateReaction(id,userName,reaction));
    }
}
