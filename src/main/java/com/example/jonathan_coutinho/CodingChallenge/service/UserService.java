package com.example.jonathan_coutinho.CodingChallenge.service;

import com.example.jonathan_coutinho.CodingChallenge.domain.Comment;
import com.example.jonathan_coutinho.CodingChallenge.domain.User;
import com.example.jonathan_coutinho.CodingChallenge.domain.UserRole;
import com.example.jonathan_coutinho.CodingChallenge.dto.UserDTO;
import com.example.jonathan_coutinho.CodingChallenge.repository.UserRepository;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.BadRequestException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotAuthorizedException;
import com.example.jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public final int MAX_FAILED_ATTMEPTS = 4;
    public final long MAX_LOCKED_TIME = 20*60*1000;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User createNewUser(UserDTO userDTO){
        validateUserInfo(userDTO);
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if(existingUser.isPresent()){
            if(existingUser.get().getEmail().equals(userDTO.getEmail())){
                throw new BadRequestException("Este email já está vincualdo a uma conta");
            }
            throw new BadRequestException("Este nome de usuário já foi utilizado");
        }
        Util.validatePassword(userDTO.getPassword());
        User newUser = new User(userDTO);

        setDefaultInfo(newUser);
        return userRepository.save(newUser);
    }

    public List<User> getAllUsers(){
        List<User> list = userRepository.findAll();
        if(list.isEmpty()) throw new NotFoundException("Nenhum usuário cadastrado");
        return list;
    }

    public User getUserByName(String name){
        Optional<User> existingUser = userRepository.findByUsername(name);
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

    public List<Comment> getAllCommentsOfUser(String username){
        Optional<User> existingUser = userRepository.findByUsername(username);
        if(existingUser.isEmpty()){
            throw new NotFoundException("Usuário não encontrado");
        }
        return existingUser.get().getCommentaries();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public User upgradeUserRole(String provider,String receiver){
        if(userRepository.findByUsername(provider).isEmpty()) throw new NotFoundException("Moderador não encontrado");
        if(!userRepository.findByUsername(provider).get().getRole().equals(UserRole.MODERADOR)){
            throw new NotAuthorizedException("Somente usuários moderadores podem performar essa ação");
        }
        Optional<User> newModerator = userRepository.findByUsername(receiver);
        if (newModerator.isEmpty()) throw new NotFoundException("Usuário não encontrado");

        newModerator.get().setRole(UserRole.MODERADOR);
        return newModerator.get();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void validateUserInfo(UserDTO userDTO){
        if(userDTO.getUsername().isBlank() || userDTO.getUsername() == null ||
                userDTO.getEmail().isBlank() || userDTO.getEmail() == null ||
                userDTO.getPassword().isBlank() || userDTO.getPassword() == null){
            throw new BadRequestException("Os dados informados possuem campos faltantes");
        }
    }

    public void setDefaultInfo(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setRole(UserRole.LEITOR);
    }

    public User buffPoints(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        user.setPoints(user.getPoints() + 21);
        user.pointsHandler();
        return userRepository.save(user);
    }

    public void updateUserFailedAttempts(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        int updatedFailedAttempts = user.getFailedAttempts() + 1;
        userRepository.updateFailedAttempts(user.getUsername(),updatedFailedAttempts);
    }

    public void lockUserAccount(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        user.setAccountNonLocked(false);
        user.setLock_time(new Date());
        userRepository.save(user);
    }

    public void unlockUserAccount(String username) {
        long timeNowInMilli = System.currentTimeMillis();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (timeNowInMilli - user.getLock_time().getTime() > MAX_LOCKED_TIME) {
            user.setFailedAttempts(0);
            user.setAccountNonLocked(true);
            user.setLock_time(null);
            userRepository.save(user);
        }
    }
}
