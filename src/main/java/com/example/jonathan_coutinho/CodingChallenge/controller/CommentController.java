package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.dto.CommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ReplyCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@Api(tags = {"Comentários"})
@Tag(name = "Comentários", description = "Endpoint para controle de comentários feitos pelos usuários")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation("Criação de comentários")
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody NewCommentDTO newCommentDTO){
        return ResponseEntity.ok(commentService.createCommentary(newCommentDTO));
    }

    @ApiOperation("Criação de resposta a comentários")
    @PostMapping("/{id}/reply")
    public ResponseEntity<Comment> replyComment(@RequestBody NewCommentDTO newCommentDTO, @PathVariable Long id){
        ReplyCommentDTO replyCommentDTO = new ReplyCommentDTO(id, newCommentDTO.getUserId(), newCommentDTO.getMovieId(), newCommentDTO.getCommentary(),newCommentDTO.getTimeNDate());
        return ResponseEntity.ok(commentService.replyCommentary(replyCommentDTO));
    }

    @ApiOperation("Targetar um comentário como duplicado")
    @PatchMapping("/moderation/user={userId}/comment={commentId}")
    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    public ResponseEntity<Comment> flagAsDuplicate(@PathVariable Long userId,@PathVariable Long commentId){
        return ResponseEntity.ok(commentService.flagAsDuplicateComment(commentId,userId));
    }

    @ApiOperation("Buscar um comentário específico")
    @GetMapping("/{id}")
    public ResponseEntity<List<CommentDTO>> getComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @ApiOperation("Atualizar o número de 'curtidas' de um comentário")
    @PatchMapping("/adv/cmmt={id}/user={userName}-rct={reaction}")
    @PreAuthorize("hasAnyRole('ROLE_AVANCADO','ROLE_MODERADOR')")
    public ResponseEntity<Comment> patchReaction(@PathVariable Long id, @PathVariable String userName, @PathVariable String reaction){
        return ResponseEntity.ok(commentService.updateReaction(id,userName,reaction));
    }
}
