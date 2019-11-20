package takred.setionpoc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
@RestController
@RequestMapping(value = "/")
public class LoginController {
    private Map<UUID, SessionGuess> mapSessionGuess = new HashMap<>();

    private final AccountService accountService;
    private final GuessService guessService;
    private final TicTacToeService ticTacToeService;

    public LoginController(AccountService accountService, GuessService guessService, TicTacToeService ticTacToeService) {
        this.accountService = accountService;
        this.guessService = guessService;
        this.ticTacToeService = ticTacToeService;
    }

    private Map<String, List<ResultTicTacToe>> getMapHistoryTicTacToe() {
        return ticTacToeService.getMapHistoryTicTacToe();
    }

    private Map<String, List<ResultGuess>> getMapHistoryGuess(){
        return guessService.getMapHistoryGuess();
    }

    @RequestMapping(value = "/register/{loginName}")
    public RegisterResponse register(@PathVariable("loginName") String loginName) {
        if (!accountService.getMapAccount().containsKey(loginName)) {
            UUID loginSessionId = UUID.randomUUID();
            accountService.getMapAccount().put(loginName, new Account(loginName, loginSessionId,
                    true, false, null, null));
            getMapHistoryGuess().put(loginName, new ArrayList<>());
            getMapHistoryTicTacToe().put(loginName, new ArrayList<>());
            return new RegisterResponse(loginSessionId, "Добро пожаловать. ");
        }
        return new RegisterResponse(null, "Логин уже сущетвует.");
    }

    @RequestMapping(value = "/login/{loginName}")
    public RegisterResponse login(@PathVariable("loginName") String loginName) {
        Account account = accountService.getMapAccount().get(loginName);
        if (accountService.getMapAccount().containsKey(loginName)) {
            System.out.println(loginName + " " + account.getLoginStatus());
            if (!account.getLoginStatus()) {
                UUID loginSessionId = UUID.randomUUID();
                accountService.getMapAccount().put(loginName, account
                        .withLoginSessionId(loginSessionId)
                        .withLoginStatus(true)
                        .withGameStatus(false)
                );
                return new RegisterResponse(loginSessionId, "");
            } else {
                return new RegisterResponse(null, "Нужно сначала разлогиниться.");
            }
        }
        return new RegisterResponse(null, "Логин не найден.");
    }

//    public String terminate(UUID id) {
//        if (mapSessionGuess.containsKey(id)) {
//            mapSessionGuess.remove(id);
//            return "Session terminate";
//        }
//        return "Session does not exist";
//    }
//
//    @RequestMapping(value = "/logout/{loginName}")
//    public logoutResponse logout(@PathVariable("loginName") String loginName) {
//        System.out.println("Выход.");
//        Account account = accountService.getMapAccount().get(loginName);
//        if (accountService.getMapAccount().containsKey(loginName)) {
//            if (account.getLoginStatus()) {
//                if (account.getGameStatus()) {
//                    List<ResultGuess> resultGuesses = new ArrayList<>(mapHistoryGuess.get(loginName));
//                    resultGuesses.set(resultGuesses.size() - 1,
//                            new ResultGuess(loginName, account.getSessionIdGuess(),
//                                    mapSessionGuess.get(account.getSessionIdGuess()).getCountTry(),
//                                    resultGuesses.get(resultGuesses.size() - 1).getWin()));
//                    mapHistoryGuess.put(loginName, resultGuesses);
//                    terminate(account.getSessionIdGuess());
//                    accountService.getMapAccount().put(loginName, account
//                            .withGameStatus(false)
//                            .withSessionIdGuess(null)
//                            .withLoginStatus(false)
//                            .withLoginSessionId(null)
//                    );
//                    System.out.println("Вышло 1");
//                    return new logoutResponse(true, "Игра прервана. Увидимся вновь!");
//                } else {
//                    System.out.println("Вышло 2");
//                    accountService.getMapAccount().put(loginName, account.withLoginStatus(false));
//                    return new logoutResponse(true, "До свидания!");
//                }
//            } else {
//                System.out.println("Не вышло 3");
//                return new logoutResponse(false, "Сначала надо войти.");
//            }
//        }
//        System.out.println("Не вышло 4");
//        return new logoutResponse(false, "Такого логина не существует.");
//    }
//
//    public AccountService getAccountService() {
//        return accountService;
//    }
//
//    public Map<UUID, SessionGuess> getMapSessionGuess() {
//        return mapSessionGuess;
//    }
//
//    public Map<String, List<ResultTicTacToe>> getMapHistoryTicTacToe() {
//        return mapHistoryTicTacToe;
//    }
}
