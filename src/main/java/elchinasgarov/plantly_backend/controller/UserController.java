package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.model.MyUser;
import elchinasgarov.plantly_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private  UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MyUser user){
        MyUser registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MyUser user) {
        try {
            Map<String, String> tokens = userService.login(user);
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(userService.refreshAccessToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        String username = authentication.getName();
        userService.logout(username);
        return ResponseEntity.ok("Successfully logged out.");
    }


}
