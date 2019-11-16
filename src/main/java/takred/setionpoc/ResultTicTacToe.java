package takred.setionpoc;

import java.util.UUID;

public class ResultTicTacToe {
    private final UUID sessionIdTicTacToe;
    private final String nameFirstPlayer;
    private final String nameSecondPlayer;
    private final String winner;
    private final String endMessage;


    public ResultTicTacToe(UUID sessionIdTicTacToe, String nameFirstPlayer, String nameSecondPlayer, String winner, String endMessage) {
        this.sessionIdTicTacToe = sessionIdTicTacToe;
        this.nameFirstPlayer = nameFirstPlayer;
        this.nameSecondPlayer = nameSecondPlayer;
        this.winner = winner;
        this.endMessage = endMessage;
    }

    public UUID getSessionIdTicTacToe() {
        return sessionIdTicTacToe;
    }

    public ResultTicTacToe withSessionIdTicTacToe(UUID sessionIdTicTacToe) {
        return new ResultTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, winner, endMessage);
    }

    public String getNameFirstPlayer() {
        return nameFirstPlayer;
    }

    public ResultTicTacToe withNameFirstPlayer(String nameFirstPlayer) {
        return new ResultTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, winner, endMessage);
    }

    public String getNameSecondPlayer() {
        return nameSecondPlayer;
    }

    public ResultTicTacToe withNameSecondPlayer(String nameSecondPlayer) {
        return new ResultTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, winner, endMessage);
    }

    public String getWinner() {
        return winner;
    }

    public ResultTicTacToe withWinner(String winner) {
        return new ResultTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, winner, endMessage);
    }

    public String getEndMessage() {
        return endMessage;
    }

    public ResultTicTacToe withEndMessage(String endMessage) {
        return new ResultTicTacToe(sessionIdTicTacToe, nameFirstPlayer, nameSecondPlayer, winner, endMessage);
    }
}