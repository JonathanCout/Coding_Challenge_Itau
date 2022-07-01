package jonathan_coutinho.CodingChallenge.controller;

import jonathan_coutinho.CodingChallenge.domain.User;
import jonathan_coutinho.CodingChallenge.dto.UserDTO;
import jonathan_coutinho.CodingChallenge.service.AuthenticationService;
import jonathan_coutinho.CodingChallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

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
    public ResponseEntity<User> login() throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) authenticationService.loadUserByUsername(username);
        List<String> jwtTokens =
                authenticationService.createJWTTokens(user.getUsername(),
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        return ResponseEntity.status(OK)
                .header(AUTHORIZATION,jwtTokens.get(0))
                .header(AUTHORIZATION,jwtTokens.get(1))
                .body(user);

    }

    @GetMapping("/refresh_token")
    public ResponseEntity<String> refreshUserToken(HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.refreshAccessToken(request));
    }
}
