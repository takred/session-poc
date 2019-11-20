package takred.setionpoc.guess;

import java.util.UUID;

public class ResultGuess {
    private final String loginName;
    private UUID gameSessionId;
    private final Integer attempts;
    private final boolean win;

    public ResultGuess(String loginName, UUID gameSessionId , Integer attempts, boolean win){
        this.loginName = loginName;
        this.gameSessionId = gameSessionId;
        this.attempts = attempts;
        this.win = win;
    }

    public String getLoginName() {
        return loginName;
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public ResultGuess withGameSessionId(UUID gameSessionId) {
        return  new ResultGuess(loginName, gameSessionId, attempts, win);
    }

    public ResultGuess withLoginName(String loginName) {
        return  new ResultGuess(loginName, gameSessionId, attempts, win);
    }

    public Integer getAttempts() {
        return attempts;
    }

    public ResultGuess withAttempts(Integer attempts) {
        return  new ResultGuess(loginName, gameSessionId, attempts, win);
    }

    public boolean getWin() {
        return win;
    }

    public ResultGuess withWin(boolean win) {
        return  new ResultGuess(loginName, gameSessionId, attempts, win);
    }
}
