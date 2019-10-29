package takred.setionpoc;

import java.util.UUID;

public class Session {
    private final UUID gameSessionId;
    private final Integer countTry;
    private final Integer randomNumber;

    public Session(UUID gameSessionId, Integer countTry, Integer randomNumber) {
        this.gameSessionId = gameSessionId;
        this.countTry = countTry;
        this.randomNumber = randomNumber;
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public Session setGameSessionId(UUID gameSessionId) {
        return new Session(gameSessionId, countTry, randomNumber);
    }

    public Integer getCountTry() {
        return countTry;
    }

    public Session setCountTry(Integer countTry) {
        return new Session(gameSessionId, countTry, randomNumber);
    }

    public Integer getRandomNumber() {
        return randomNumber;
    }
}
