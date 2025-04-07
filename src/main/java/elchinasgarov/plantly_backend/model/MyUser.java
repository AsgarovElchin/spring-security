package elchinasgarov.plantly_backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 512)
    private String refreshToken;

    @Column(length = 6)
    private String resetOtp;

    private LocalDateTime resetOtpExpiry;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRefreshToken() { return refreshToken; }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getResetOtp() { return resetOtp; }

    public void setResetOtp(String resetOtp) { this.resetOtp = resetOtp; }

    public LocalDateTime getResetOtpExpiry() { return resetOtpExpiry; }

    public void setResetOtpExpiry(LocalDateTime resetOtpExpiry) { this.resetOtpExpiry = resetOtpExpiry; }
}