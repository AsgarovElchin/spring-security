package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.model.MyUser;
import elchinasgarov.plantly_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        String token = userService.verify(user);
        if (token.equals("Fail")) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        return ResponseEntity.ok(token);
    }
}
