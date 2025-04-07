package elchinasgarov.plantly_backend.service;


import elchinasgarov.plantly_backend.model.MyUser;
import elchinasgarov.plantly_backend.model.UserPrincipal;
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
    private final EmailService emailService;
    private final VerifiedEmailRepository verifiedEmailRepository;

    public UserService(UserRepository userRepository, JWTService jwtService,
                       PasswordEncoder passwordEncoder, EmailService emailService, VerifiedEmailRepository verifiedEmailRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verifiedEmailRepository = verifiedEmailRepository;
    }

    public MyUser register(MyUser user) {
        String email = user.getEmail().toLowerCase();

        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already registered.");
        }

        if (!verifiedEmailRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Please verify your email before registering.");
        }

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyUser savedUser = userRepository.save(user);

        verifiedEmailRepository.deleteByEmail(email);
        return savedUser;
    }

    public Map<String, String> login(MyUser user) {
        MyUser existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

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

    public void sendPasswordResetOtp(String email) {
        MyUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String otp = OtpUtil.generate6DigitOtp();
        user.setResetOtp(otp);
        user.setResetOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        String message = "Your password reset OTP is: " + otp + "\nIt will expire in 10 minutes.";
        emailService.sendEmail(email, "Password Reset OTP", message);
    }

    public void resetPasswordWithOtp(String email, String otp, String newPassword) {
        MyUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getResetOtp() == null || !user.getResetOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getResetOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setResetOtpExpiry(null);
        userRepository.save(user);
    }
}
