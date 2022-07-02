package jonathan_coutinho.CodingChallenge.controller;

import jonathan_coutinho.CodingChallenge.domain.Comment;
import jonathan_coutinho.CodingChallenge.dto.CommentDTO;
import jonathan_coutinho.CodingChallenge.dto.NewCommentDTO;
import jonathan_coutinho.CodingChallenge.dto.ReplyCommentDTO;
import jonathan_coutinho.CodingChallenge.dto.ResponseDTO;
import jonathan_coutinho.CodingChallenge.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody NewCommentDTO newCommentDTO, HttpServletResponse response) throws IOException {
        if(newCommentDTO.getComment().contains("{comment-}")){
            response.sendRedirect("/comment/quote");
            return null;
        }
        return ResponseEntity.ok(commentService.createComment(newCommentDTO));
    }

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

    @PutMapping("/moderation/flagDuplicate")
    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    public ResponseEntity<Comment> flagAsDuplicate(@RequestParam Long commentId){
        return ResponseEntity.ok(commentService.flagAsDuplicateComment(commentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentDTO>> getComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @PutMapping("/advanced/updateReaction")
    @PreAuthorize("hasAnyRole('ROLE_AVANCADO','ROLE_MODERADOR')")
    public ResponseEntity<Comment> putReaction(@RequestParam Long id,@RequestParam String reaction){
        return ResponseEntity.ok(commentService.updateReaction(id,reaction));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteComment(@RequestParam Long id, @RequestParam String username){
        commentService.deleteCommentById(id,username);
        return ResponseEntity.ok(new ResponseDTO(ResponseDTO.Status.SUCCESS,"Comentário deletado"));
    }


}
