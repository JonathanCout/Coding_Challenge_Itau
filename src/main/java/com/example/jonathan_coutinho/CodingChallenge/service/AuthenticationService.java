package com.example.jonathan_coutinho.CodingChallenge.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.UserRepository;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.BadRequestException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements UserDetailsService {

    @Value("${sdt.security.jwt.secret}")
    private String secret;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public String refreshAccessToken(HttpServletRequest request){

        String refreshToken = request.getHeader(AUTHORIZATION);
        if(refreshToken==null) throw new BadRequestException("Erro ao atualizar token: nenhum token encontrado");

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String username = decodedJWT.getSubject();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
            return JWT.create()
                    .withSubject(user.getEmail())
                    .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .withIssuer("codingChallenge")
                    .withIssuedAt(new Date())
                    .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                    .sign(algorithm);

        } catch (Exception e) {
            log.error("Error refreshing access token: {}", e.getMessage());
            throw new BadRequestException(format("Erro ao atualizar token de acesso: Mensagem de erro: %s",e.getMessage()));
        }
    }

    public User findAuthenticatedUser(HttpServletRequest request,
                                      HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        log.info("Tokens: {}",request.getHeader(AUTHORIZATION));
        log.info("Current User Name: {}",currentUserName);
        log.info("Credentials: {}",authentication.getCredentials());
        log.info("Principal: {}",authentication.getPrincipal());

        return userRepository.findByUsername(currentUserName).orElseThrow(() ->
                new NotFoundException("Usuário não encontrado"));
    }

    public List<String> createJWTTokens(String subject, List<String> roles){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String access_token = JWT.create()
                .withSubject(subject)
                .withClaim("roles",roles)
                .withIssuer("codingChallenge")
                .withIssuedAt(java.sql.Date.from(Instant.now()))
                .withExpiresAt(java.sql.Date.valueOf(LocalDateTime.now().plusDays(1).toLocalDate()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(subject)
                .withIssuer("codingChallenge")
                .withIssuedAt(java.sql.Date.from(Instant.now()))
                .withExpiresAt(java.sql.Date.valueOf(LocalDateTime.now().plusDays(5).toLocalDate()))
                .sign(algorithm);
        return List.of(access_token,refresh_token);
    }

    public UserDTO validateUser(HttpServletRequest request,
                                HttpServletResponse response) {

        return new UserDTO(findAuthenticatedUser(request,response));

    }
}
