package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.domain.UserRole;
import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.UserRepository;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.BadRequestException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotAuthorizedException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createNewUser(UserDTO userDTO){
        validateUserInfo(userDTO);
        Optional<User> existingUser = userRepository.findByUserName(userDTO.getUserName());
        if(existingUser.isPresent()){
            if(existingUser.get().getEmail().equals(userDTO.getEmail())){
                throw new BadRequestException("Este email já está vincualdo a uma conta");
            }
            throw new BadRequestException("Este nome de usuário já foi utilizado");
        }

        User newUser = new User(userDTO);
        newUser.setRole(UserRole.LEITOR);
        newUser.setCommentaries(new ArrayList<>());
        return userRepository.save(newUser);
    }

    public List<User> getAllUsers(){
        List<User> list = userRepository.findAll();
        if(list.isEmpty()) throw new NotFoundException("Nenhum usuário cadastrado");
        return list;
    }

    public User getUserByName(String name){
        Optional<User> existingUser = userRepository.findByUserName(name);
        if(existingUser.isEmpty()){
            throw new NotFoundException("Usuário não encontrado");
        }
        return existingUser.get();
    }

    public User getUserByEmail(String email){
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isEmpty()) throw new NotFoundException("Usuário não encontrado");
        return existingUser.get();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public User upgradeUserRole(String provider,String receiver){
        if(userRepository.findByUserName(provider).isEmpty()) throw new NotFoundException("Moderador não encontrado");
        if(!userRepository.findByUserName(provider).get().getRole().equals(UserRole.MODERADOR)){
            throw new NotAuthorizedException("Somente usuários moderadores podem performar essa ação");
        }
        Optional<User> newModerator = userRepository.findByUserName(receiver);
        if (newModerator.isEmpty()) throw new NotFoundException("Usuário não encontrado");

        newModerator.get().setRole(UserRole.MODERADOR);
        return newModerator.get();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void validateUserInfo(UserDTO userDTO){
        if(userDTO.getUserName().isBlank() || userDTO.getUserName() == null ||
                userDTO.getEmail().isBlank() || userDTO.getEmail() == null ||
                userDTO.getPassword().isBlank() || userDTO.getPassword() == null){
            throw new BadRequestException("Os dados informados possuem campos faltantes");
        }
    }

    public User buffPoints(String username){
        User user = userRepository.findByUserName(username).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        user.setPoints(user.getPoints() + 21);
        user.pointsHandler();
        return userRepository.save(user);
    }
}
