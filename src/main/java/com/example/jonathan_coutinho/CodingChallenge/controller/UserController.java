package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("user")
@Api(tags = {"Usuário"})
@Tag(name = "Usuário",description = "Endpoint criado para controlar criações e busca de usuários")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation("Encontra um usuário a partir do seu username.")
    @GetMapping("/username={username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByName(username));
    }

    @ApiOperation("Encontra um usuário a partir do email dele.")
    @GetMapping("/email={email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @ApiOperation("Retorna a lista completa de comentários feita por um usuário específico")
    @GetMapping("/{username}-comments")
    public ResponseEntity<List<Comment>> getAllCommentsByUser(@PathVariable String username){
        return ResponseEntity.ok(userService.getAllCommentsOfUser(username));
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> buffUser(@PathVariable String username){
        return ResponseEntity.ok(userService.buffPoints(username));
    }
}
