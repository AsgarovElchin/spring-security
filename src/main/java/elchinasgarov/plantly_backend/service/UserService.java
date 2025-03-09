package elchinasgarov.plantly_backend.service;


import elchinasgarov.plantly_backend.model.MyUser;
import elchinasgarov.plantly_backend.repository.UserRepository;
import elchinasgarov.plantly_backend.util.UserAlreadyExistsException;
import elchinasgarov.plantly_backend.util.WeakPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register user with validation and encrypted password
    public MyUser register(MyUser user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already taken!");
        }

        if (isWeakPassword(user.getPassword())) {
            throw new WeakPasswordException("Choose a stronger password!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ✅ Login and return JWT token
    public String verify(MyUser user) {
        MyUser existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return jwtService.generateToken(user.getUsername());
    }

    // ✅ Prevent weak passwords
    private boolean isWeakPassword(String password) {
        List<String> commonPasswords = List.of("123456", "password", "qwerty", "letmein");
        return commonPasswords.contains(password) || password.length() < 8;
    }
}
