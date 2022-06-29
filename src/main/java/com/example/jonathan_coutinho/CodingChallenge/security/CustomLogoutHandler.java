package com.example.jonathan_coutinho.CodingChallenge.security;

import com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.jonathan_coutinho.CodingChallenge.dto.ResponseDTO.Status.SUCCESS;

@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder
                .getContext()
                .setAuthentication(null);
        Cookie refreshTokenCookie = new Cookie("refresh_token","");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
        log.info("User has been logged out");
        try {
            new ObjectMapper().writeValue(response.getOutputStream(),
                    new ResponseDTO(SUCCESS,"Usuário foi deslogado."));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
