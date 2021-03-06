package jonathan_coutinho.CodingChallenge.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jonathan_coutinho.CodingChallenge.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/login")||request.getServletPath().equals("/signup")||request.getServletPath().equals("/refresh_token")){
            filterChain.doFilter(request,response);
        }else {
            String requestTokens = request.getHeader(AUTHORIZATION);
            if(requestTokens == null){
                response.setStatus(403);
                new ObjectMapper().writeValue(response.getOutputStream(), new ResponseDTO(ResponseDTO.Status.FAILED,"Token de acesso não encontrado"));
                return;
            }
            try{
                Algorithm algorithm = Algorithm.HMAC256(secret);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(requestTokens);
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
                        new ResponseDTO(ResponseDTO.Status.FAILED,e.getMessage()));
                return;
            }
            filterChain.doFilter(request,response);
        }

    }
}
