package com.example.jonathan_coutinho.CodingChallenge.security;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
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


public class JwtCredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public JwtCredentialsAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        this.userService = new UserService();
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try{
            CredentialsRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), CredentialsRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken
                    (authenticationRequest.getUsername(),authenticationRequest.getPassword());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(cacheNames = "FailedLogin", key = "#request.getParameter('username')")
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        userService.unlockUserAccount(request.getParameter("username"));
        chain.doFilter(request,response);
    }

    @Override
    @Cacheable(cacheNames = "FailedLogin", key = "#request.getParameter('username')")
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String username = request.getParameter("username");
        User user = userService.getUserByName(username);
        if (user.isEnabled() && user.isAccountNonLocked()) {
            if (user.getFailedAttempts() < userService.MAX_FAILED_ATTMEPTS) {
                failed = new LockedException(
                        String.format("%d(s) tentativa(s) de login falharam. A conta será bloqueada em %d tentativas.",
                                user.getFailedAttempts(), userService.MAX_FAILED_ATTMEPTS - user.getFailedAttempts()));
                userService.updateUserFailedAttempts(user.getUsername());
            } else {
                failed = new LockedException("Esta conta está bloqueada devido a máxima tentativas de login sem sucesso em pouco tempo. A conta será liberada em 20 minutos");
                userService.lockUserAccount(user.getUsername());
            }
        }
        userService.unlockUserAccount(user.getUsername());
        failed = new LockedException("A conta foi desbloquada. Tente fazer o login novamente");
    }
}
