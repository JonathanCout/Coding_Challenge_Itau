package com.example.jonathan_coutinho.CodingChallenge.domain;

import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user")
    private List<Comment> commentaries;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user")
    private List<Score> scores;

    public User(UserDTO userDTO) {
        this.userName = userDTO.getUserName();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
    }

    public String checkRole(){
        if(this.getRole() == UserRole.MODERADOR){
            return this.getRole().name();
        }
        if(this.getPoints() >= 1000){
            this.setRole(UserRole.MODERADOR);
        } else if (this.getPoints() >= 100){
            this.setRole(UserRole.AVANCADO);
        } else if (this.getPoints() >= 20){
            this.setRole(UserRole.BASICO);
        }
        return this.getRole().name();
    }
    public void pointsHandler(){
        this.setPoints(this.getPoints()+1);
        this.checkRole();
    }
}
