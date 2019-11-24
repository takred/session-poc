package takred.setionpoc;

public class LogoutResponse {
    private boolean logoutStatus;
    private String message;

    public LogoutResponse(boolean logoutStatus, String message) {
        this.logoutStatus = logoutStatus;
        this.message = message;
    }

    public boolean getLogoutStatus() {
        return logoutStatus;
    }

    public LogoutResponse setLogoutStatus(boolean logoutStatus) {
        return new LogoutResponse(logoutStatus, message);
    }

    public String getMessage() {
        return message;
    }

    public LogoutResponse setMessage(String message) {
        return new LogoutResponse(logoutStatus, message);
    }
}
