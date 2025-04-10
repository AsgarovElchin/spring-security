package elchinasgarov.plantly_backend.repository;

import elchinasgarov.plantly_backend.model.OtpType;
import elchinasgarov.plantly_backend.model.VerifiedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerifiedEmailRepository extends JpaRepository<VerifiedEmail, Long> {
    Optional<VerifiedEmail> findByEmailAndType(String email, OtpType type);
    void deleteByEmailAndType(String email, OtpType type);
}