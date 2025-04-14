package elchinasgarov.plantly_backend.service;


import com.google.api.client.util.Value;
import elchinasgarov.plantly_backend.model.*;
import elchinasgarov.plantly_backend.repository.UserRepository;
import elchinasgarov.plantly_backend.repository.VerifiedEmailRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;

@Service
public class UserService {


    @Value("${google.client-id}")
    private String googleClientId;


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

        // 1. Check if user exists
        if (existingUser == null) {
            throw new RuntimeException("Invalid email or password");
        }

        // 2. Disallow Google users from logging in with password
        if (existingUser.getProvider() != AuthProvider.CUSTOM) {
            throw new RuntimeException("Please login using " + existingUser.getProvider().name());
        }

        // 3. Check password
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 4. Generate JWT tokens
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        existingUser.setRefreshToken(refreshToken);
        userRepository.save(existingUser);

        // 5. Return tokens
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

        verifiedEmailRepository.delete(verified.get());
    }

    public Map<String, String> loginWithGoogle(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(Utils.getDefaultTransport(), Utils.getDefaultJsonFactory())
                    .setAudience(Collections.singletonList(googleClientId)) // from Google Cloud Console
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) throw new RuntimeException("Invalid ID token");

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail().toLowerCase();

            MyUser user = userRepository.findByEmail(email);
            if (user == null) {
                user = new MyUser();
                user.setEmail(email);
                user.setPassword(""); // Optional: password not required for Google users
                user.setProvider(AuthProvider.GOOGLE);
                user = userRepository.save(user);
            }

            String accessToken = jwtService.generateAccessToken(email);
            String refreshToken = jwtService.generateRefreshToken(email);

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return tokens;

        } catch (Exception e) {
            throw new RuntimeException("Google token verification failed: " + e.getMessage());
        }
    }
}