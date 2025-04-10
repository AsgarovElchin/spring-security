package elchinasgarov.plantly_backend.service;

import elchinasgarov.plantly_backend.model.Otp;
import elchinasgarov.plantly_backend.model.OtpType;
import elchinasgarov.plantly_backend.model.VerifiedEmail;
import elchinasgarov.plantly_backend.repository.OtpRepository;
import elchinasgarov.plantly_backend.repository.VerifiedEmailRepository;
import elchinasgarov.plantly_backend.util.OtpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final VerifiedEmailRepository verifiedEmailRepository;

    public OtpService(OtpRepository otpRepository, EmailService emailService, VerifiedEmailRepository verifiedEmailRepository) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
        this.verifiedEmailRepository = verifiedEmailRepository;
    }

    @Transactional
    public void generateOtp(String email, OtpType type) {
        String otp = OtpUtil.generate6DigitOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        otpRepository.deleteByEmailAndType(email.toLowerCase(), type);
        Otp newOtp = new Otp(email.toLowerCase(), otp, expiry, type);
        otpRepository.save(newOtp);

        String subject = (type == OtpType.REGISTRATION) ? "Email Verification OTP" : "Password Reset OTP";
        String message = "Your OTP is: " + otp + ". It will expire in 5 minutes.";
        emailService.sendEmail(email, subject, message);
    }

    @Transactional
    public boolean verifyOtp(String email, String inputOtp, OtpType type) {
        return otpRepository.findByEmailAndType(email.toLowerCase(), type)
                .filter(stored -> stored.getOtp().equals(inputOtp))
                .filter(stored -> stored.getExpiryTime().isAfter(LocalDateTime.now()))
                .map(validOtp -> {
                    otpRepository.delete(validOtp);
                    verifiedEmailRepository.save(new VerifiedEmail(email, type));
                    return true;
                })
                .orElse(false);
    }

    public boolean isEmailVerified(String email, OtpType type) {
        return verifiedEmailRepository.findByEmailAndType(email, type).isPresent();
    }

    public void clearVerification(String email, OtpType type) {
        verifiedEmailRepository.deleteByEmailAndType(email, type);
    }
}