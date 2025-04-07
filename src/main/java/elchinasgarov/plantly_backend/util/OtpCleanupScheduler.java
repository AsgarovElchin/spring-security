package elchinasgarov.plantly_backend.util;

import elchinasgarov.plantly_backend.repository.OtpRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OtpCleanupScheduler {

    private final OtpRepository otpRepository;

    public OtpCleanupScheduler(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void deleteExpiredOtps() {
        otpRepository.findAll().stream()
                .filter(otp -> otp.getExpiryTime().isBefore(LocalDateTime.now()))
                .forEach(otpRepository::delete);
    }
}
