package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.dto.*;
import elchinasgarov.plantly_backend.model.MyUser;
import elchinasgarov.plantly_backend.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto dto) {
        try {
            MyUser user = new MyUser();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());

            MyUser registeredUser = userService.register(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", registeredUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto dto) {
        try {
            MyUser user = new MyUser();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());

            Map<String, String> tokens = userService.login(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", tokens));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ApiErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDto request) {
        try {
            String accessToken = userService.refreshAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(new ApiResponse<>(true, "Access token refreshed", accessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ApiErrorResponse(e.getMessage()));
        }
    }


    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        try {
            String email = authentication.getName();
            userService.logout(email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully logged out", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiErrorResponse("Logout failed"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto dto) {
        try {
            userService.resetPassword(dto.getEmail(), dto.getNewPassword());
            return ResponseEntity.ok(new ApiResponse<>(true, "Password successfully reset", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/auth/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequestDto dto) {
        try {
            Map<String, String> tokens = userService.loginWithGoogle(dto.getIdToken());
            return ResponseEntity.ok(new ApiResponse<>(true, "Google login successful", tokens));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ApiErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiErrorResponse("Internal server error"));
        }
    }

}