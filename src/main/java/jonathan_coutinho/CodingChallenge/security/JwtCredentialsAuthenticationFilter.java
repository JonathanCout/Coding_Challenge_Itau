package jonathan_coutinho.CodingChallenge.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jonathan_coutinho.CodingChallenge.domain.User;
import jonathan_coutinho.CodingChallenge.dto.ResponseDTO;
import jonathan_coutinho.CodingChallenge.service.AccountCacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Slf4j
public class JwtCredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AccountCacheConfig accountCacheConfig;
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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request,response);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountCacheConfig.onCacheExpiration(user.getUsername());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        try{
            accountCacheConfig.getUsername(username);
        } catch (ExecutionException ex){
            log.info("Não foi possível cachear a solicitação");
        }
        response.setStatus(401);
        new ObjectMapper().writeValue(response.getOutputStream(),
                new ResponseDTO(ResponseDTO.Status.FAILED,accountCacheConfig.checkCacheHits(username)));
    }
}
