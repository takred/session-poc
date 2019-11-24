package takred.setionpoc;

import org.springframework.stereotype.Service;
import takred.setionpoc.guess.GuessService;
import takred.setionpoc.guess.ResultGuess;
import takred.setionpoc.tictactoe.TicTacToeService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public logoutResponse logout(String loginName) {
        System.out.println("Выход.");
        Account account = accountService.getMapAccount().get(loginName);
        if (accountService.getMapAccount().containsKey(loginName)) {
            if (account.getLoginStatus()) {
                if (account.getGameStatus()) {
                    List<ResultGuess> resultGuesses = new ArrayList<>(guessService.getMapHistoryGuess().get(loginName));
                    resultGuesses.set(resultGuesses.size() - 1,
                            new ResultGuess(loginName, account.getSessionIdGuess(),
                                    guessService.getMapSessionGuess().get(account.getSessionIdGuess()).getCountTry(),
                                    resultGuesses.get(resultGuesses.size() - 1).getWin()));
                    guessService.getMapHistoryGuess().put(loginName, resultGuesses);
                    guessService.terminate(account.getSessionIdGuess());
                    accountService.getMapAccount().put(loginName, account
                            .withGameStatus(false)
                            .withSessionIdGuess(null)
                            .withLoginStatus(false)
                            .withLoginSessionId(null)
                    );
                    System.out.println("Вышло 1");
                    return new logoutResponse(true, "Игра прервана. Увидимся вновь!");
                } else {
                    System.out.println("Вышло 2");
                    accountService.getMapAccount().put(loginName, account.withLoginStatus(false));
                    return new logoutResponse(true, "До свидания!");
                }
            } else {
                System.out.println("Не вышло 3");
                return new logoutResponse(false, "Сначала надо войти.");
            }
        }
        System.out.println("Не вышло 4");
        return new logoutResponse(false, "Такого логина не существует.");
    }

//    public String terminate(UUID id) {
//        if (guessService.getMapSessionGuess().containsKey(id)) {
//            guessService.getMapSessionGuess().remove(id);
//            return "Session terminate";
//        }
//        return "Session does not exist";
//    }

}
