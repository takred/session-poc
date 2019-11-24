package takred.setionpoc;

import org.springframework.stereotype.Service;
import takred.setionpoc.guess.GuessService;
import takred.setionpoc.guess.ResultGuess;
import takred.setionpoc.tictactoe.TicTacToeService;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogoutService {

    AccountService accountService;
    GuessService guessService;
    TicTacToeService ticTacToeService;

    public LogoutService(AccountService accountService, GuessService guessService, TicTacToeService ticTacToeService) {
        this.accountService = accountService;
        this.guessService = guessService;
        this.ticTacToeService = ticTacToeService;
    }

    public LogoutResponse logout(String loginName) {
        System.out.println("Выход.");
        Account account = accountService.getMapAccount().get(loginName);
        if (!accountService.getMapAccount().containsKey(loginName)) {
            System.out.println("Не вышло 4");
            return new LogoutResponse(false, "Такого логина не существует.");
        }
        if (!account.getLoginStatus()) {
            System.out.println("Не вышло 3");
            return new LogoutResponse(false, "Сначала надо войти.");
        }
        if (!account.getGameStatus()) {
            System.out.println("Вышло 2");
            accountService.getMapAccount().put(loginName, account.withLoginStatus(false));
            return new LogoutResponse(true, "До свидания!");
        }
            List<ResultGuess> resultGuesses = new ArrayList<>(guessService.getMapHistoryGuess().get(loginName));
            resultGuesses.set(resultGuesses.size() - 1,
                    new ResultGuess(loginName, account.getSessionIdGuess(),
                            guessService.getMapSessionGuess().get(account.getSessionIdGuess()).getCountTry(),
                            resultGuesses.get(resultGuesses.size() - 1).getWin()));
            guessService.getMapHistoryGuess().put(loginName, resultGuesses);
            guessService.terminate(account.getLoginSessionId());
            accountService.getMapAccount().put(loginName, account
                    .withGameStatus(false)
                    .withSessionIdGuess(null)
                    .withLoginStatus(false)
                    .withLoginSessionId(null)
            );
            System.out.println("Вышло 1");
            return new LogoutResponse(true, "Игра прервана. Увидимся вновь!");
        }
    }
