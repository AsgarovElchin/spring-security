package elchinasgarov.plantly_backend.service;

import elchinasgarov.plantly_backend.model.Otp;
import elchinasgarov.plantly_backend.repository.OtpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;

    public OtpService(OtpRepository otpRepository, EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void generateOtp(String rawEmail) {
        String email = rawEmail.trim().toLowerCase();
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        otpRepository.deleteByEmail(email);
        Otp newOtp = new Otp(email, otp, expiry);
        otpRepository.save(newOtp);

        emailService.sendOtpEmail(email, otp);
    }

    public boolean verifyOtp(String rawEmail, String inputOtp) {
        String email = rawEmail.trim().toLowerCase();

        return otpRepository.findByEmail(email)
                .filter(stored -> stored.getOtp().equals(inputOtp))
                .filter(stored -> stored.getExpiryTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}