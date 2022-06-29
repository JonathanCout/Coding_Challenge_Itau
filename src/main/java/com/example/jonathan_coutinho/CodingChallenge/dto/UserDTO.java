package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String username;
    private String email;
    private String password;

    public UserDTO(User user){
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
