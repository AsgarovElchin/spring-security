package elchinasgarov.plantly_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VerifiedEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDateTime verifiedAt;

    public VerifiedEmail() {}

    public VerifiedEmail(String email) {
        this.email = email;
        this.verifiedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }
}
