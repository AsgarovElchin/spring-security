package elchinasgarov.plantly_backend.service;

import elchinasgarov.plantly_backend.model.Otp;
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
    public void generateRegistrationOtp(String email) {
        String otp = OtpUtil.generate6DigitOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        otpRepository.deleteByEmail(email.toLowerCase());
        Otp newOtp = new Otp(email.toLowerCase(), otp, expiry);
        otpRepository.save(newOtp);

        String message = "Your verification OTP is: " + otp + ". It will expire in 5 minutes.";
        emailService.sendEmail(email, "Email Verification OTP", message);
    }

    @Transactional
    public boolean verifyRegistrationOtp(String email, String inputOtp) {
        return otpRepository.findByEmail(email.toLowerCase())
                .filter(stored -> stored.getOtp().equals(inputOtp))
                .filter(stored -> stored.getExpiryTime().isAfter(LocalDateTime.now()))
                .map(validOtp -> {
                    otpRepository.delete(validOtp);
                    verifiedEmailRepository.save(new VerifiedEmail(email));
                    return true;
                })
                .orElse(false);
    }
}
