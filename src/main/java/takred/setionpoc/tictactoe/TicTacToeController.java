package takred.setionpoc.tictactoe;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import takred.setionpoc.*;

import java.util.*;

@RestController
@RequestMapping(value = "/tic_tac_toe")
public class TicTacToeController {
    //    private boolean symbol = true;
    private List<List<String>> table;
    public List<String> resultGame = new ArrayList<>();
    private int countGame = 0;
    private final AccountService accountService;
    private final TicTacToeService ticTacToeService;

    public TicTacToeController(AccountService accountService, TicTacToeService ticTacToeService) {
        this.accountService = accountService;
        this.ticTacToeService = ticTacToeService;
    }

    public Map<String, Account> getMapAccount() {
        return accountService.getMapAccount();
    }

    private Map<UUID, SessionTicTacToe> getMapSessionTicTacToe() {
        return ticTacToeService.getMapSessionTicTacToe();
    }

    private Map<String, List<ResultTicTacToe>> getMapHistoryTicTacToe() {
        return ticTacToeService.getMapHistoryTicTacToe();
    }

    @RequestMapping(value = "/start/{nameFirstPlayer}/{nameSecondPlayer}")
    public RegisterResponse startTicTacToe(@PathVariable("nameFirstPlayer") String nameFirstPlayer,
                                           @PathVariable("nameSecondPlayer") String nameSecondPlayer) {
        return ticTacToeService.startTicTacToe(nameFirstPlayer, nameSecondPlayer);
    }

    //    /*
    @RequestMapping(value = "/play/{sessionIdTicTacToe}/{login}/{x}/{y}")
    public List<List<String>> game(@PathVariable("sessionIdTicTacToe") UUID sessionIdTicTacToe,
                                   @PathVariable("login") String login,
                                   @PathVariable("x") int x,
                                   @PathVariable("y") int y) {
        return ticTacToeService.game(sessionIdTicTacToe, login, x, y);
    }/**/

    @RequestMapping(value = "/users")
    public List<Account> usersTicTacToe() {
        return ticTacToeService.usersTicTacToe();
    }

    @RequestMapping(value = "/users/{userName}/history")
    public String historyTicTacToe(@PathVariable("userName") String userName) {
        return ticTacToeService.historyTicTacToe(userName);
    }

    @RequestMapping(value = "/users/{userName}/history/{numberGame}")
    public String resultTicTacToe(@PathVariable("userName") String userName, @PathVariable("numberGame") int numberGame) {
        return ticTacToeService.resultTicTacToe(userName, numberGame);
    }

    @RequestMapping(value = "/play")
    public List<List<String>> newGame() {
        return ticTacToeService.newGame();
    }
}
