package takred.setionpoc;

public class RegisterResponseGuess {
    private final String result;
    private final String message;

    public RegisterResponseGuess(String result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
