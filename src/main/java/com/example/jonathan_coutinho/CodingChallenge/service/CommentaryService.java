package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Commentary;
import com.example.jonathan_coutinho.CodingChallenge.domain.Movie;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.domain.UserRole;
import com.example.jonathan_coutinho.CodingChallenge.dto.CommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.NewCommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.dto.ReplyCommentaryDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.CommentaryRepository;
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
public class CommentaryService {

    @Autowired
    private CommentaryRepository commentaryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    public Commentary createCommentary(NewCommentaryDTO newCommentaryDTO) {
        User user = userRepository.findById(newCommentaryDTO.getUserId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        Movie movie = movieRepository.findById(newCommentaryDTO.getMovieId()).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        if(newCommentaryDTO.getCommentary().isEmpty()){
                return commentaryRepository.save(new Commentary(user,movie, newCommentaryDTO.getScore()));
        }
        if(user.getRole().equals(UserRole.LEITOR)) throw
                new NotAuthorizedException("Usuários Leitores não podem postar comentários");
        return commentaryRepository.save(new Commentary(newCommentaryDTO,user,movie));
    }

    public Commentary replyCommentary(ReplyCommentaryDTO replyCommentaryDTO){
        User user = userRepository.findById(replyCommentaryDTO.getUserId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        Movie movie = movieRepository.findById(replyCommentaryDTO.getMovieId()).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        if(user.getRole().equals(UserRole.LEITOR)) throw
                new NotAuthorizedException("Usuários Leitores não podem postar comentários");
        user.pointsHandler();
        return commentaryRepository.save(new Commentary(replyCommentaryDTO,user,movie));
    }

    public List<CommentaryDTO> getComment(Long id){
        Commentary commentary = commentaryRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado."));

        if(commentary.getPreviousId() != null){
            Commentary oldCommentary = commentaryRepository.findById(commentary.getPreviousId()).
                    orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
            return List.of(new CommentaryDTO(oldCommentary),new CommentaryDTO(commentary));
        }
        return List.of(new CommentaryDTO(commentary));
    }

    public List<CommentaryDTO> getCommentariesByUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        List<Commentary> commentaries = commentaryRepository.findAllByUser(user);

        if(commentaries.isEmpty()){
            throw new BadRequestException("O usuário ainda não possui comentários");
        }
        return commentaries.stream().map(CommentaryDTO::new).collect(Collectors.toList());
    }

    public List<CommentaryDTO> getCommentariesByMovie(Long id){
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NotFoundException("Filme não encontrado."));
        List<Commentary> commentaries = commentaryRepository.findAllByMovie(movie);

        if(commentaries.isEmpty()){
            throw new BadRequestException("O filme ainda não possuí comentários");
        }
        return commentaries.stream().map(CommentaryDTO::new).collect(Collectors.toList());
    }

    public Commentary updateReaction(Long id,String userName, Integer reaction){
        Optional<User> user = userRepository.findByName(userName);
        if(user.isEmpty()) throw new NotFoundException("Usuário não encontrado");
        if(user.get().getRole().equals(UserRole.BASICO) || user.get().getRole().equals(UserRole.LEITOR)){
            throw new NotAuthorizedException("Somente usuários Avançados e Moderadores podem reagir a comentários");
        }
        Commentary commentary = commentaryRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
        commentary.setReaction(commentary.getReaction() + reaction);
        return commentaryRepository.save(commentary);
    }

    public void deleteCommentary(Long id, Long userId){
        Commentary commentary = commentaryRepository.findById(id).orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não enontrado"));
        if(!user.getRole().equals(UserRole.MODERADOR)) throw new NotAuthorizedException("Somente moderadores podem deletar comentários");
        commentaryRepository.deleteById(commentary.getId());
    }

}
