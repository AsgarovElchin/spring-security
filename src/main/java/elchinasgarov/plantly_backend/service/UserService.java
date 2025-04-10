package elchinasgarov.plantly_backend.service;


import elchinasgarov.plantly_backend.model.*;
import elchinasgarov.plantly_backend.repository.UserRepository;
import elchinasgarov.plantly_backend.repository.VerifiedEmailRepository;
import elchinasgarov.plantly_backend.util.OtpUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final OtpService otpService;

    public UserService(UserRepository userRepository, JWTService jwtService,
                       PasswordEncoder passwordEncoder, VerifiedEmailRepository verifiedEmailRepository,
                       OtpService otpService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.verifiedEmailRepository = verifiedEmailRepository;
        this.otpService = otpService;
    }

    public MyUser register(MyUser user) {
        String email = user.getEmail().toLowerCase();

        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already registered.");
        }

        if (!otpService.isEmailVerified(email, OtpType.REGISTRATION)) {
            throw new RuntimeException("Please verify your email before registering.");
        }

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyUser savedUser = userRepository.save(user);

        otpService.clearVerification(email, OtpType.REGISTRATION);
        return savedUser;
    }

    public Map<String, String> login(MyUser user) {
        MyUser existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        existingUser.setRefreshToken(refreshToken);
        userRepository.save(existingUser);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    public String refreshAccessToken(String refreshToken) {
        Optional<MyUser> userOptional = userRepository.findByRefreshToken(refreshToken);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid refresh token");
        }

        MyUser user = userOptional.get();
        UserDetails userDetails = new UserPrincipal(user);

        try {
            if (!jwtService.validateRefreshToken(refreshToken, userDetails)) {
                throw new RuntimeException("Invalid or expired refresh token");
            }
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Refresh token has expired");
        }

        return jwtService.generateAccessToken(user.getEmail());
    }

    @Transactional
    public void logout(String username) {
        MyUser user = userRepository.findByEmail(username);
        if (user != null) {
            user.setRefreshToken(null);
            userRepository.save(user);
        }
    }

    public void resetPassword(String email, String newPassword) {
        Optional<VerifiedEmail> verified = verifiedEmailRepository.findByEmailAndType(email.toLowerCase(), OtpType.PASSWORD_RESET);

        if (verified.isEmpty()) {
            throw new RuntimeException("OTP verification required before resetting password.");
        }

        MyUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        verifiedEmailRepository.delete(verified.get()); // Clear verification after password reset
    }
}