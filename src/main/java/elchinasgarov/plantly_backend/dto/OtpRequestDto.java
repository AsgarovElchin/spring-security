package elchinasgarov.plantly_backend.dto;

import elchinasgarov.plantly_backend.model.OtpType;

public class OtpRequestDto {

    private String email;
    private OtpType type;

    public OtpRequestDto() {}

    public OtpRequestDto(String email, OtpType type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OtpType getType() {
        return type;
    }

    public void setType(OtpType type) {
        this.type = type;
    }
}