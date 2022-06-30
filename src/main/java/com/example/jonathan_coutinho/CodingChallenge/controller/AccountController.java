package com.example.jonathan_coutinho.CodingChallenge.controller;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import com.example.jonathan_coutinho.CodingChallenge.service.AuthenticationService;
import com.example.jonathan_coutinho.CodingChallenge.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Api(tags = {"Conta"})
@Tag(name = "Conta",description = "Endpoint criado para controlar criações e login de usuários")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @ApiOperation("Faz a criação de um usuário")
    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        User user = userService.createNewUser(userDTO);
        List<String> jwtTokens = authenticationService.createJWTTokens(user.getUsername(),List.of(user.getRole().name()));

        return ResponseEntity.status(HttpStatus.OK)
                .header(AUTHORIZATION, jwtTokens.get(0))
                .header(AUTHORIZATION, jwtTokens.get(1))
                .body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login() throws IOException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) authenticationService.loadUserByUsername(username);
        List<String> jwtTokens =
                authenticationService.createJWTTokens(user.getUsername(),
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        UserDTO responseData = new UserDTO(user);

        return ResponseEntity.status(OK)
                .header(AUTHORIZATION,jwtTokens.get(0))
                .header(AUTHORIZATION,jwtTokens.get(1))
                .body(responseData);

    }

    @GetMapping("/refresh_token")
    public ResponseEntity<String> refreshUserToken(HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.refreshAccessToken(request));
    }
}
