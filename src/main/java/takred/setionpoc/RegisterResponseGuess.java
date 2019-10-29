package takred.setionpoc;

public class RegisterResponseGuess {
    private final String result;
    private final Integer count;
    private final String message;

    public RegisterResponseGuess(String result, Integer count, String message) {
        this.result = result;
        this.count = count;
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public Integer getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }
}
