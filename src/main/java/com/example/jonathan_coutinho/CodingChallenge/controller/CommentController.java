package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.dto.CommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ReplyCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody NewCommentDTO newCommentDTO){
        return ResponseEntity.ok(commentService.createCommentary(newCommentDTO));
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<Comment> replyComment(@RequestBody NewCommentDTO newCommentDTO, @PathVariable Long id){
        ReplyCommentDTO replyCommentDTO = new ReplyCommentDTO(id, newCommentDTO.getUserId(), newCommentDTO.getMovieId(), newCommentDTO.getCommentary(),newCommentDTO.getTimeNDate());
        return ResponseEntity.ok(commentService.replyCommentary(replyCommentDTO));
    }

    @PatchMapping("/user={userId}/comment={commentId}")
    public ResponseEntity<Comment> flagAsDuplicate(@PathVariable Long userId,@PathVariable Long commentId){
        return ResponseEntity.ok(commentService.flagAsDuplicateComment(commentId,userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentDTO>> getComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUser(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getCommentariesByUser(id));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByMovie(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getCommentariesByMovie(id));
    }

    @PatchMapping("/{id}/{userName}?={reaction}")
    public ResponseEntity<Comment> patchReaction(@PathVariable Long id, @PathVariable String userName, @PathVariable Integer reaction){
        return ResponseEntity.ok(commentService.updateReaction(id,userName,reaction));
    }
}
