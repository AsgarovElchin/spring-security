package elchinasgarov.plantly_backend.repository;

import elchinasgarov.plantly_backend.model.Otp;
import elchinasgarov.plantly_backend.model.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmailAndType(String email, OtpType type);
    void deleteByEmailAndType(String email, OtpType type);
}