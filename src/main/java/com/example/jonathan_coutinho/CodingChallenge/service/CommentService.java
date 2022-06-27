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
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.BadRequestException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotAuthorizedException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    public Comment createCommentary(NewCommentDTO newCommentDTO) {
        User user = userRepository.findById(newCommentDTO.getUserId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        Movie movie = movieRepository.findByImobid(newCommentDTO.getMovieId()).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        if(user.getRole().equals(UserRole.LEITOR)) throw
                new NotAuthorizedException("Usuários Leitores não podem postar comentários");
        return commentRepository.save(new Comment(newCommentDTO,user,movie));
    }

    public Comment replyCommentary(ReplyCommentDTO replyCommentDTO){
        User user = userRepository.findById(replyCommentDTO.getUserId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        Movie movie = movieRepository.findByImobid(replyCommentDTO.getMovieId()).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        if(user.getRole().equals(UserRole.LEITOR)) throw
                new NotAuthorizedException("Usuários Leitores não podem postar comentários");
        user.pointsHandler();
        return commentRepository.save(new Comment(replyCommentDTO,user,movie));
    }

    public List<CommentDTO> getComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado."));

        if(comment.getPreviousId() != null){
            Comment oldComment = commentRepository.findById(comment.getPreviousId()).
                    orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
            return List.of(new CommentDTO(oldComment),new CommentDTO(comment));
        }
        return List.of(new CommentDTO(comment));
    }

    public List<CommentDTO> getCommentariesByUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        List<Comment> commentaries = commentRepository.findAllByUser(user);

        if(commentaries.isEmpty()){
            throw new BadRequestException("O usuário ainda não possui comentários");
        }
        return commentaries.stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentariesByMovie(Long id){
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        List<Comment> commentaries = commentRepository.findAllByMovie(movie);

        if(commentaries.isEmpty()){
            throw new BadRequestException("O filme ainda não possuí comentários");
        }
        return commentaries.stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    public Comment updateReaction(Long id, String userName, Integer reaction){
        Optional<User> user = userRepository.findByUserName(userName);
        if(user.isEmpty()) throw new NotFoundException("Usuário não encontrado");
        if(user.get().getRole().equals(UserRole.BASICO) || user.get().getRole().equals(UserRole.LEITOR)){
            throw new NotAuthorizedException("Somente usuários Avançados e Moderadores podem reagir a comentários");
        }
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
        comment.setReaction(comment.getReaction() + reaction);
        return commentRepository.save(comment);
    }

    public Comment flagAsDuplicateComment(Long id, Long userId){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não enontrado"));
        if(!user.getRole().equals(UserRole.MODERADOR)) throw new NotAuthorizedException("Somente moderadores podem marcar comentários como duplicados");
        comment.setDuplicate(true);
        return commentRepository.save(comment);
    }

}
