package takred.setionpoc;

import java.util.UUID;

public class Account {
    private final String loginName;
    private final UUID loginSessionId;
    private final boolean loginStatus;
    private final boolean gameStatus;
    private final UUID sessionIdGuess;
    private final UUID sessionIdTicTacTo;


    public Account(String loginName, UUID loginSessionId, boolean loginStatus, boolean gameStatus, UUID sessionIdGuess, UUID sessionIdTicTacTo) {
        this.loginName = loginName;
        this.loginSessionId = loginSessionId;
        this.loginStatus = loginStatus;
        this.gameStatus = gameStatus;
        this.sessionIdGuess = sessionIdGuess;
        this.sessionIdTicTacTo = sessionIdTicTacTo;
    }

    public String getLoginName() {
        return loginName;
    }

    public Account setLoginName(String loginName) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, sessionIdGuess, sessionIdTicTacTo);
    }

    public UUID getLoginSessionId() {
        return loginSessionId;
    }

    public Account withLoginSessionId(UUID loginSessionId) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, sessionIdGuess, sessionIdTicTacTo);
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public Account withLoginStatus(boolean loginStatus) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, sessionIdGuess, sessionIdTicTacTo);
    }

    public boolean getGameStatus() {
        return gameStatus;
    }

    public Account withGameStatus(boolean gameStatus) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, sessionIdGuess , sessionIdTicTacTo);
    }

    public UUID getSessionIdGuess() {
        return sessionIdGuess;
    }

    public Account withSessionIdGuess(UUID sessionIdGuess) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, sessionIdGuess, sessionIdTicTacTo);
    }

    public UUID getSessionIdTicTacToe() {
        return sessionIdTicTacTo;
    }

    public Account withSessionIdTicTacToe(UUID sessionIdTicTacTo) {
        return new Account(loginName, loginSessionId, loginStatus, gameStatus, sessionIdGuess, sessionIdTicTacTo);
    }
}
