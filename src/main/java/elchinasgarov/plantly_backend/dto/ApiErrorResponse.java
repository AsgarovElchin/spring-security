package elchinasgarov.plantly_backend.dto;

public class ApiErrorResponse {
    private boolean success;
    private String message;

    public ApiErrorResponse(String message) {
        this.success = false;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}