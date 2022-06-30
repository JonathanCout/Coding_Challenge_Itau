package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.domain.UserRole;
import com.example.jonathan_coutinho.CodingChallenge.dto.CommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ReplyCommentDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.CommentRepository;
import com.example.jonathan_coutinho.CodingChallenge.repository.MovieRepository;
import com.example.jonathan_coutinho.CodingChallenge.repository.UserRepository;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotAuthorizedException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {


    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public Comment createComment(NewCommentDTO newCommentDTO) {
        User user = userRepository.findById(newCommentDTO.getUserId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        Movie movie = movieRepository.findByImdbid(newCommentDTO.getMovieId()).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        Comment comment = new Comment(newCommentDTO,user,movie);
        comment.setDuplicate(false);
        comment.setReaction(0);

        user.pointsHandler();
        userRepository.save(user);

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment createReplyComment(ReplyCommentDTO replyCommentDTO){
        User user = userRepository.findById(replyCommentDTO.getUserId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        Movie movie = movieRepository.findByImdbid(replyCommentDTO.getMovieId()).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        Comment comment = new Comment(replyCommentDTO,user,movie);
        comment.setDuplicate(false);

        user.pointsHandler();
        userRepository.save(user);

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment createQuoteComment(NewCommentDTO newCommentDTO){
        User user = userRepository.findById(newCommentDTO.getUserId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        Movie movie = movieRepository.findByImdbid(newCommentDTO.getMovieId()).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        ReplyCommentDTO replyCommentDTO = new ReplyCommentDTO(newCommentDTO, findIdsFromComment(newCommentDTO.getComment()));
        Comment comment = new Comment(replyCommentDTO,user,movie);
        comment.setDuplicate(false);

        user.pointsHandler();
        userRepository.save(user);
        return  commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado."));

        List<CommentDTO> commentsList = new ArrayList<>();
        commentsList.add(new CommentDTO(comment));

        if(comment.getPreviousId() != null){
            for(Long previousId : comment.getPreviousId()){
                Comment oldComment = commentRepository.findById(previousId).
                        orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
                commentsList.add(new CommentDTO(oldComment));
            }
            return commentsList;
        }
        return commentsList;
    }

    @Transactional
    public Comment updateReaction(Long id, String username, String reaction){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
        if(reaction.equals("like")){
            comment.setReaction(comment.getReaction() + 1);
        }
        if(reaction.equals("dislike")){
            comment.setReaction(comment.getReaction() - 1);
        }
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteCommentById(Long commentId, String username){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comentário não encontrado"));

        if(!comment.getUser().getUsername().equals(username) && !comment.getUser().getRole().equals(UserRole.MODERADOR)){
            throw new NotAuthorizedException("Somente usuários moderadores podem deletar comentários não próprios");
        }
        try{
            log.info("Tentando deletar o comentário de id {}",commentId);
            commentRepository.deleteById(commentId);
        }catch (EmptyResultDataAccessException ex){
            log.info("Houve uma tentativa de deletar o comentário, porém ele não foi encontrado");
        }
    }

    @Transactional
    public Comment flagAsDuplicateComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
        comment.setDuplicate(true);
        return commentRepository.save(comment);
    }

    public List<Long> findIdsFromComment(String comment){
        List<Long> idList = new ArrayList<>();
        int firstIndex = comment.indexOf("{comment-");
        int lasIndex = comment.indexOf("}",firstIndex);
        long id = Long.parseLong(comment.substring(firstIndex + 9, lasIndex));
        idList.add(id);
        while (firstIndex >= 0){
            firstIndex = comment.indexOf("{comment-");
            lasIndex = comment.indexOf("}",firstIndex);
            id = Long.parseLong(comment.substring(firstIndex + 9, lasIndex));
            idList.add(id);
        }
        return idList;
    }
}
