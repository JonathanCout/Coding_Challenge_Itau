package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import com.example.jonathan_coutinho.CodingChallenge.service.AuthenticationService;
import com.example.jonathan_coutinho.CodingChallenge.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@Api(tags = {"Usuário"})
@Tag(name = "Usuário",description = "Endpoint criado para controlar criações e busca de usuários")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;


    @ApiOperation("Faz a criação de um usuário")
    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        User user = userService.createNewUser(userDTO);
        List<ResponseCookie> jwtCookies = authenticationService.createJWTCookies(user.getUsername(),List.of(user.getRole().name()));

        return ResponseEntity.status(HttpStatus.OK)
                .header(SET_COOKIE, jwtCookies.get(0).toString())
                .header(SET_COOKIE, jwtCookies.get(1).toString())
                .build();
    }

    @ApiOperation("Encontra um usuário a partir do seu username.")
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByName(username));
    }

    @ApiOperation("Encontra um usuário a partir do email dele.")
    @GetMapping("/{email}")
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
