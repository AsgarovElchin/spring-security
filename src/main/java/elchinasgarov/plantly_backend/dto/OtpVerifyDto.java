package elchinasgarov.plantly_backend.dto;

import elchinasgarov.plantly_backend.model.OtpType;

public class OtpVerifyDto {

    private String email;
    private String otp;
    private OtpType type;

    public OtpVerifyDto() {}

    public OtpVerifyDto(String email, String otp, OtpType type) {
        this.email = email;
        this.otp = otp;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public OtpType getType() {
        return type;
    }

    public void setType(OtpType type) {
        this.type = type;
    }
}