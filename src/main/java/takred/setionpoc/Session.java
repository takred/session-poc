package takred.setionpoc;

import java.util.UUID;

public class Session {
    private final UUID gameSessionId;
    private final Integer countLogin;
    private final Integer randomNumber;

    public Session(UUID gameSessionId, Integer countLogin, Integer randomNumber) {
        this.gameSessionId = gameSessionId;
        this.countLogin = countLogin;
        this.randomNumber = randomNumber;
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public Session setGameSessionId(UUID gameSessionId) {
        return new Session(gameSessionId, countLogin, randomNumber);
    }

    public Integer getCountLogin() {
        return countLogin;
    }

    public Session setCountLogin(Integer countLogin) {
        return new Session(gameSessionId, countLogin, randomNumber);
    }

    public Integer getRandomNumber() {
        return randomNumber;
    }
}
