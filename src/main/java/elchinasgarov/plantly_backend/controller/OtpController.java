package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.dto.OtpRequestDto;
import elchinasgarov.plantly_backend.dto.OtpVerifyDto;
import elchinasgarov.plantly_backend.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequestDto dto) {
        otpService.generateOtp(dto.getEmail());
        return ResponseEntity.ok("OTP sent to email.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyDto dto) {
        boolean isValid = otpService.verifyOtp(dto.getEmail(), dto.getOtp());
        return isValid ?
                ResponseEntity.ok("OTP verified.") :
                ResponseEntity.badRequest().body("Invalid or expired OTP.");
    }
}
