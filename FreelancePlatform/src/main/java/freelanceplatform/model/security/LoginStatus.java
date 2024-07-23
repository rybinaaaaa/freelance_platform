package freelanceplatform.model.security;

public class LoginStatus {
    private boolean loggedIn;
    private Integer userId;
    private String username;
    private String errorMessage;
    private boolean success;

    public LoginStatus() {
    }

    public LoginStatus(boolean loggedIn, boolean success, Integer userId, String username, String errorMessage) {
        this.loggedIn = loggedIn;
        this.userId = userId;
        this.username = username;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {this.userId = userId;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

