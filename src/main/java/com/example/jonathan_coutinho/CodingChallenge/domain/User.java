package com.example.jonathan_coutinho.CodingChallenge.domain;

import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;
    private int points;
    private UserRole role;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Commentary> commentaries;

    public User(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.userName = userDTO.getUserName();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.role = UserRole.LEITOR;
    }

    public String checkRole(){
        if(this.role == UserRole.MODERADOR){
            return this.getRole().name();
        }
        if(this.points >= 1000){
            this.setRole(UserRole.MODERADOR);
        } else if (this.points >= 100){
            this.setRole(UserRole.AVANCADO);
        } else if (this.points >= 20){
            this.setRole(UserRole.BASICO);
        }
        return this.getRole().name();
    }
    public void pointsHandler(){
        this.points++;
        checkRole();
    }
}
