package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User createNewUser(UserDTO userDTO){
        return new User(userDTO);
    }
    public List<User> getAllUsers(){
        return repository.findAll();
    }
    public User getUserByName(String name){
        return repository.findByName(name);
    }
    public Optional<User> getUserById(Long id){
        return repository.findById(id);
    }
    public void deleteUser(Long id){
        repository.deleteById(id);
    }

}
