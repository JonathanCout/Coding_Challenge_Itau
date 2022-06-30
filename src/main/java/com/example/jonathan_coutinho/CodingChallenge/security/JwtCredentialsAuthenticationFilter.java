package com.example.jonathan_coutinho.CodingChallenge.security;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO;
import com.example.jonathan_coutinho.CodingChallenge.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO.Status.FAILED;

@RequiredArgsConstructor
public class JwtCredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private String username;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try{
            CredentialsRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), CredentialsRequest.class);
            username = authenticationRequest.getUsername();
            Authentication authentication = new UsernamePasswordAuthenticationToken
                    (authenticationRequest.getUsername(),authenticationRequest.getPassword());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(cacheNames = "FailedLogin", key = "#username")
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request,response);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.unlockUserAccount(user.getUsername());
    }

    @Override
    @Cacheable(cacheNames = "FailedLogin", key = "#username")
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String errorMessage = "Conta bloqueada devido a múltiplas tentativas de login sem sucesso sucessivamente";
        User user = userService.getUser(username);
        if (user.isEnabled() && user.isAccountNonLocked()) {
            if (user.getFailedAttempts() < userService.MAX_FAILED_ATTMEPTS) {
                user.setFailedAttempts(user.getFailedAttempts() + 1);
                userService.userRepository.save(user);
                errorMessage =
                        String.format("%d(s) tentativa(s) de login falharam. A conta será bloqueada em %d tentativas.",
                                user.getFailedAttempts(), userService.MAX_FAILED_ATTMEPTS - user.getFailedAttempts());
            } else {
                errorMessage = "Esta conta será bloqueada devido a máxima tentativas de login sem sucesso em pouco tempo. A conta será liberada em 20 minutos";
                userService.lockUserAccount(user.getUsername());
            }
        }
        response.setStatus(401);
        new ObjectMapper().writeValue(response.getOutputStream(),
                new ResponseDTO(FAILED,errorMessage));
    }
}
