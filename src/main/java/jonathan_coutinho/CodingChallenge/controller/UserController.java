package jonathan_coutinho.CodingChallenge.controller;

import jonathan_coutinho.CodingChallenge.domain.Comment;
import jonathan_coutinho.CodingChallenge.domain.User;
import jonathan_coutinho.CodingChallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUser(username));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByUser(@RequestParam String username){
        return ResponseEntity.ok(userService.getAllCommentsOfUser(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> buffUser(@PathVariable String username){
        return ResponseEntity.ok(userService.buffPoints(username));
    }

    @PutMapping("/moderation/upgrade")
    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    public ResponseEntity<User> upgradeToModerator(@RequestParam String receiver){
        return ResponseEntity.ok(userService.upgradeUserRole(receiver));
    }
}
