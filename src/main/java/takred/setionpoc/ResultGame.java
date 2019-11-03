package takred.setionpoc;

import java.util.UUID;

public class ResultGame {
    private final String loginName;
    private UUID gameSessionId;
    private final Integer attempts;
    private final boolean win;

    public ResultGame(String loginName, UUID gameSessionId , Integer attempts, boolean win){
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

    public ResultGame withGameSessionId(UUID gameSessionId) {
        return  new ResultGame(loginName, gameSessionId, attempts, win);
    }

    public ResultGame withLoginName(String loginName) {
        return  new ResultGame(loginName, gameSessionId, attempts, win);
    }

    public Integer getAttempts() {
        return attempts;
    }

    public ResultGame withAttempts(Integer attempts) {
        return  new ResultGame(loginName, gameSessionId, attempts, win);
    }

    public boolean getWin() {
        return win;
    }

    public ResultGame withWin(boolean win) {
        return  new ResultGame(loginName, gameSessionId, attempts, win);
    }
}
