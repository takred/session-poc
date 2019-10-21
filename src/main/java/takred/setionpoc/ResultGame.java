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

    public ResultGame setGameSessionId(UUID gameSessionId) {
        return  new ResultGame(loginName, gameSessionId, attempts, win);
    }

    public ResultGame setLoginName(String loginName) {
        return  new ResultGame(loginName, gameSessionId, attempts, win);
    }

    public Integer getAttempts() {
        return attempts;
    }

    public ResultGame setAttempts(Integer attempts) {
        return  new ResultGame(loginName, gameSessionId, attempts, win);
    }

    public boolean getWin() {
        return win;
    }

    public ResultGame setWin(boolean win) {
        return  new ResultGame(loginName, gameSessionId, attempts, win);
    }
}
