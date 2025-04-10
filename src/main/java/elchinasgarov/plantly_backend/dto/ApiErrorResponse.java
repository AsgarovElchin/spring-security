package elchinasgarov.plantly_backend.dto;


public class ApiErrorResponse {
    private String message;
    private boolean success = false;

    public ApiErrorResponse() {}

    public ApiErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}