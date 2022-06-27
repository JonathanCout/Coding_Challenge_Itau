package com.example.jonathan_coutinho.CodingChallenge.dto;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String userName;
    private String email;
    private String password;
    private String role;

    public UserDTO(User user){
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole().toString();
    }
}
