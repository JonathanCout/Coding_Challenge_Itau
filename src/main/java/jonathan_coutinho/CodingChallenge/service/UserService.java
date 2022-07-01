package jonathan_coutinho.CodingChallenge.service;

import jonathan_coutinho.CodingChallenge.domain.Comment;
import jonathan_coutinho.CodingChallenge.domain.User;
import jonathan_coutinho.CodingChallenge.domain.UserRole;
import jonathan_coutinho.CodingChallenge.dto.UserDTO;
import jonathan_coutinho.CodingChallenge.repository.UserRepository;
import jonathan_coutinho.CodingChallenge.service.exceptions.BadRequestException;
import jonathan_coutinho.CodingChallenge.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;


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

    public User getUser(String credential){
        Optional<User> existingUser;
        if(credential.contains("@")){
            existingUser = userRepository.findByEmail(credential);
        } else {
            existingUser = userRepository.findByUsername(credential);
        }
        return existingUser.orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public List<Comment> getAllCommentsOfUser(String username){
        Optional<User> existingUser = userRepository.findByUsername(username);
        return existingUser
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"))
                .getCommentaries();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public User upgradeUserRole(String receiver){
        User newModerator = userRepository.findByUsername(receiver)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        newModerator.setRole(UserRole.MODERADOR);
        return userRepository.save(newModerator);
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
        user.pointsHandler(21);
        return userRepository.save(user);
    }

}
