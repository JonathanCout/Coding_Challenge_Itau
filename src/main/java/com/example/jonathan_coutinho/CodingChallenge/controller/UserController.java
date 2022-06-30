package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUser(username));
    }

    @ApiOperation("Retorna a lista completa de comentários feita por um usuário específico")
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByUser(@RequestParam String username){
        return ResponseEntity.ok(userService.getAllCommentsOfUser(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> buffUser(@PathVariable String username){
        return ResponseEntity.ok(userService.buffPoints(username));
    }

    @PostMapping("/moderation/upgrade")
    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    public ResponseEntity<User> upgradeToModerator(@RequestParam String provider, @RequestParam String receiver){
        return ResponseEntity.ok(userService.upgradeUserRole(provider,receiver));
    }
}
