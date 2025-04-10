package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.dto.ApiErrorResponse;
import elchinasgarov.plantly_backend.dto.ApiResponse;
import elchinasgarov.plantly_backend.dto.OtpRequestDto;
import elchinasgarov.plantly_backend.dto.OtpVerifyDto;
import elchinasgarov.plantly_backend.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequestDto dto) {
        try {
            otpService.generateOtp(dto.getEmail(), dto.getType());
            return ResponseEntity.ok(new ApiResponse<>(true, "OTP sent to email", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiErrorResponse("Failed to send OTP"));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyDto dto) {
        boolean isValid = otpService.verifyOtp(dto.getEmail(), dto.getOtp(), dto.getType());
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse<>(true, "OTP verified", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiErrorResponse("Invalid or expired OTP"));
        }
    }
}