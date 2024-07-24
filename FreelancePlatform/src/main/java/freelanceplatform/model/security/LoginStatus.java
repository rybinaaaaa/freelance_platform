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

    /**
     * Checks if the user is logged in.
     *
     * @return {@code true} if the user is logged in; {@code false} otherwise
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets the login status of the user.
     *
     * @param loggedIn {@code true} to indicate the user is logged in; {@code false} otherwise
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Returns the ID of the user.
     *
     * @return the user ID
     */
    public Integer getUserId() {return userId;}

    /**
     * Sets the ID of the user.
     *
     * @param userId the user ID to set
     */
    public void setUserId(Integer userId) {this.userId = userId;}

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the error message, if any.
     *
     * @return the error message or {@code null} if no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message.
     *
     * @param errorMessage the error message to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Checks if the operation or session was successful.
     *
     * @return {@code true} if successful; {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success status of the operation or session.
     *
     * @param success {@code true} to indicate success; {@code false} otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}

