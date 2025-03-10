package elchinasgarov.plantly_backend.service;


import elchinasgarov.plantly_backend.model.MyUser;
import elchinasgarov.plantly_backend.model.UserPrincipal;
import elchinasgarov.plantly_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public MyUser register(MyUser user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already taken!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Map<String, String> login(MyUser user) {
        MyUser existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }


        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        existingUser.setRefreshToken(refreshToken);
        userRepository.save(existingUser);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        return response;
    }


    public String refreshAccessToken(String refreshToken) {
        Optional<MyUser> userOptional = userRepository.findByRefreshToken(refreshToken);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid refresh token");
        }

        MyUser user = userOptional.get();


        UserDetails userDetails = new UserPrincipal(user);


        if (!jwtService.validateRefreshToken(refreshToken, userDetails)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        return jwtService.generateAccessToken(user.getUsername());
    }
}
