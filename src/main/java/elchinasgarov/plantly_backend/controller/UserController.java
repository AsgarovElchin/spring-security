package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.model.MyUser;
import elchinasgarov.plantly_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private  UserService userService;

    @PostMapping("/register")
    public MyUser register(@RequestBody MyUser user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody MyUser user){
        return userService.verify(user);
    }
}
