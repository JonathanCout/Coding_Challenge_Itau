package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.dto.CommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ReplyCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO;
import com.example.jonathan_coutinho.CodingChallenge.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public ResponseEntity<Comment> createComment(@RequestBody NewCommentDTO newCommentDTO, HttpServletResponse response) throws IOException {
        if(newCommentDTO.getComment().contains("{comment-}")){
            response.sendRedirect("/comment/quote");
            return null;
        }
        return ResponseEntity.ok(commentService.createComment(newCommentDTO));
    }

    @ApiOperation("Criação de resposta a comentários")
    @PostMapping("/reply")
    public ResponseEntity<Comment> replyComment(@RequestBody NewCommentDTO newCommentDTO, @RequestParam Long id){
        ReplyCommentDTO replyCommentDTO = new ReplyCommentDTO(List.of(id), newCommentDTO.getUserId(), newCommentDTO.getMovieId(), newCommentDTO.getComment(),newCommentDTO.getTimeNDate());
        return ResponseEntity.ok(commentService.createReplyComment(replyCommentDTO));
    }

    @PostMapping("/quote")
    @PreAuthorize("hasAnyRole('ROLE_AVANCADO','ROLE_MODERADOR')")
    public ResponseEntity<Comment> quoteComment(NewCommentDTO newCommentDTO){
        return ResponseEntity.ok(commentService.createQuoteComment(newCommentDTO));
    }

    @ApiOperation("Targetar um comentário como duplicado")
    @PutMapping("/moderation/flagDuplicate")
    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    public ResponseEntity<Comment> flagAsDuplicate(@RequestParam Long commentId){
        return ResponseEntity.ok(commentService.flagAsDuplicateComment(commentId));
    }

    @ApiOperation("Buscar um comentário específico")
    @GetMapping("/{id}")
    public ResponseEntity<List<CommentDTO>> getComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @ApiOperation("Atualizar o número de 'curtidas' de um comentário")
    @PutMapping("/advanced/updateReaction")
    @PreAuthorize("hasAnyRole('ROLE_AVANCADO','ROLE_MODERADOR')")
    public ResponseEntity<Comment> patchReaction(@RequestParam Long id, @RequestParam String username, @RequestParam String reaction){
        return ResponseEntity.ok(commentService.updateReaction(id,username,reaction));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteComment(@RequestParam Long id, @RequestParam String username){
        commentService.deleteCommentById(id,username);
        return ResponseEntity.ok(new ResponseDTO(ResponseDTO.Status.SUCCESS,"Comentário deletado"));
    }


}
