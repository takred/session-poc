package takred.setionpoc;

import java.util.UUID;

public class SessionTicTacToe {
    private final UUID sessionIdTicTacToe;
    private final String nameFirstPlayer;
    private final String nameSecondPlayer;
    private final boolean currentMove;


    public SessionTicTacToe(UUID sessionIdTicTacToe, String nameFirstPlayer, String nameSecondPlayer, boolean currentMove) {
        this.sessionIdTicTacToe = sessionIdTicTacToe;
        this.nameFirstPlayer = nameFirstPlayer;
        this.nameSecondPlayer = nameSecondPlayer;
        this.currentMove = currentMove;
    }

    public UUID getSessionIdTicTacToe() {
        return sessionIdTicTacToe;
    }

    public SessionTicTacToe withSessionIdTicTacToe(UUID sessionIdTicTacToe) {
        return new SessionTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, currentMove);
    }

    public String getNameFirstPlayer() {
        return nameFirstPlayer;
    }

    public SessionTicTacToe withNameFirstPlayer(String nameFirstPlayer) {
        return new SessionTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, currentMove);
    }

    public String getNameSecondPlayer() {
        return nameSecondPlayer;
    }

    public SessionTicTacToe withNameSecondPlayer(String nameSecondPlayer) {
        return new SessionTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, currentMove);
    }

    public boolean getCurrentMove() {
        return currentMove;
    }

    public SessionTicTacToe withCurrentMove(boolean currentMove) {
        return new SessionTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, currentMove);
    }
}
