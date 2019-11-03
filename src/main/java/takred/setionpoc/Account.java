package takred.setionpoc;

import java.util.UUID;

public class Account {
    private final String loginName;
    private final UUID loginSessionId;
    private final boolean loginStatus;
    private final boolean gameStatus;
    private final UUID gameSessionId;

    public Account(String loginName, UUID loginSessionId, boolean loginStatus, boolean gameStatus, UUID gameSessionId) {
        this.loginName = loginName;
        this.loginSessionId = loginSessionId;
        this.loginStatus = loginStatus;
        this.gameStatus = gameStatus;
        this.gameSessionId = gameSessionId;
    }

    public String getLoginName() {
        return loginName;
    }

    public Account setLoginName(String loginName) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, gameSessionId);
    }

    public UUID getLoginSessionId() {
        return loginSessionId;
    }

    public Account withLoginSessionId(UUID loginSessionId) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, gameSessionId);
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public Account withLoginStatus(boolean loginStatus) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, gameSessionId);
    }

    public boolean getGameStatus() {
        return gameStatus;
    }

    public Account withGameStatus(boolean gameStatus) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, gameSessionId);
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public Account withGameSessionId(UUID gameSessionId) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, gameSessionId);
    }
}
