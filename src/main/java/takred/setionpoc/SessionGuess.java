package takred.setionpoc;

import java.util.UUID;

public class SessionGuess {
    private final UUID sessionIdGuess;
    private final Integer countTry;
    private final Integer randomNumber;

    public SessionGuess(UUID sessionIdGuess, Integer countTry, Integer randomNumber) {
        this.sessionIdGuess = sessionIdGuess;
        this.countTry = countTry;
        this.randomNumber = randomNumber;
    }

    public UUID getSessionIdGuess() {
        return sessionIdGuess;
    }

    public SessionGuess withSessionIdGuess(UUID sessionIdGuess) {
        return new SessionGuess(sessionIdGuess, countTry, randomNumber);
    }

    public Integer getCountTry() {
        return countTry;
    }

    public SessionGuess withCountTry(Integer countTry) {
        return new SessionGuess(sessionIdGuess, countTry, randomNumber);
    }

    public Integer getRandomNumber() {
        return randomNumber;
    }
}
