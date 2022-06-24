package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.UserRepository;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.BadRequestException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    public void authenticateLogin(UserDTO userDTO){
        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
        if(user.isEmpty()) throw new NotFoundException("O email informado não está cadastrado.");
        if(!user.get().getPassword().equals(userDTO.getPassword()))
            throw new BadRequestException("A senha informada está incorreta");
    }
}
