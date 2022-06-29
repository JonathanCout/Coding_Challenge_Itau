package com.example.jonathan_coutinho.CodingChallenge.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO.Status.FAILED;
import static java.util.Arrays.stream;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/login")||request.getServletPath().equals("/signup")||request.getServletPath().equals("/refreshtoken")){
            filterChain.doFilter(request,response);
        }else {
            Cookie[] requestCookies = request.getCookies();
            if(requestCookies == null){
                filterChain.doFilter(request,response);
                return;
            }
            String token = null;
            for(Cookie cookie : requestCookies){
                if(cookie.getName().equals("access_token")) token = cookie.getValue();
            }
            if(token == null){
                response.setStatus(403);
                new ObjectMapper().writeValue(response.getOutputStream(), new ResponseDTO(FAILED,"Cookie de acesso não encontrado"));
                return;
            }
            try{
                Algorithm algorithm = Algorithm.HMAC256(secret);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(token);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role ->
                        authorities.add(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username,null,authorities));
            }catch (Exception e){
                log.error("Error logging in: {}", e.getMessage());
                response.setStatus(403);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        new ResponseDTO(FAILED,e.getMessage()));
                return;
            }
            filterChain.doFilter(request,response);
        }

    }
}
