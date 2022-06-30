package com.example.jonathan_coutinho.CodingChallenge.security;

import com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO.Status.FAILED;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.warn("User: " + auth.getName()
                    + " attempted to access the protected URL: "
                    + request.getRequestURI());
        }
        log.error("Error : {}",accessDeniedException.getMessage());
        response.setStatus(403);
        Map<String, String> data = Map.of("exception: ", accessDeniedException.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(),
                new ResponseDTO(FAILED, data, "Acesso negado"));
    }

}
