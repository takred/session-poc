package takred.setionpoc;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/")
public class SessionController {
    private Map<UUID, SessionGuess> mapSessionGuess = new HashMap<>();
    private Map<UUID, SessionTicTacToe> mapSessionTicTacToe = new HashMap<>();
    private Map<String, Account> mapAccount = new HashMap<>();
    private final AccountService accountService;
    //    private Map<String, Account> mapAccountTicTacToe = new HashMap<>();
    private Map<String, List<ResultGuess>> mapHistoryGuess = new HashMap<>();
    private Map<String, List<ResultTicTacToe>> mapHistoryTicTacToe = new HashMap<>();

    private boolean symbol = true;
    private List<List<String>> table;
    public List<String> resultGame = new ArrayList<>();
    private int countGame = 0;

    public SessionController(AccountService accountService) {
        this.accountService = accountService;
    }

    private Map<String, Account> getMapAccount() {
        return accountService.getMapAccount();
    }

    @RequestMapping(value = "/start_guess/{loginSessionId}")
    public RegisterResponse startGuess(@PathVariable("loginSessionId") UUID loginSessionId) {
        Account account = getMapAccount().values().stream()
                .filter(a -> a.getLoginSessionId() != null)
                .filter(a -> a.getLoginSessionId()
                        .equals(loginSessionId))
                .findAny().orElseGet(null);
        RegisterResponse registerResponse;
        if (account != null) {
            if (!getMapAccount().get(account.getLoginName()).getGameStatus()) {
                UUID gameSessionId = UUID.randomUUID();
                mapSessionGuess.put(gameSessionId
                        , new SessionGuess(
                                gameSessionId, 0
                                , ThreadLocalRandom.current().nextInt(0, 10000)
                        )
                );
                getMapAccount().put(account.getLoginName()
                        , new Account(
                                account.getLoginName(), account.getLoginSessionId()
                                , account.getLoginStatus(), true, gameSessionId, account.getSessionIdTicTacToe()
                        )
                );
                List<ResultGuess> resultGuesses = new ArrayList<>(mapHistoryGuess.get(account.getLoginName()));
                resultGuesses.add(new ResultGuess(account.getLoginName(), gameSessionId, 0, false));
                mapHistoryGuess.put(account.getLoginName(), resultGuesses);
                registerResponse = new RegisterResponse(gameSessionId, "");
                return registerResponse;
            } else {
                registerResponse = new RegisterResponse(null, "Сначала закончите предыдущую игру!");
                return registerResponse;
            }
        }
        registerResponse = new RegisterResponse(null, "Нужно сначала залогиниться.");
        return registerResponse;
    }

    @RequestMapping(value = "/register/{loginName}")
    public RegisterResponse register(@PathVariable("loginName") String loginName) {
        if (!getMapAccount().containsKey(loginName)) {
            UUID loginSessionId = UUID.randomUUID();
            getMapAccount().put(loginName, new Account(loginName, loginSessionId,
                    true, false, null, null));
            mapHistoryGuess.put(loginName, new ArrayList<>());
            mapHistoryTicTacToe.put(loginName, new ArrayList<>());
            return new RegisterResponse(loginSessionId, "Добро пожаловать. ");
        }
        return new RegisterResponse(null, "Логин уже сущетвует.");
    }

    @RequestMapping(value = "/login/{loginName}")
    public RegisterResponse login(@PathVariable("loginName") String loginName) {
        Account account = getMapAccount().get(loginName);
        if (getMapAccount().containsKey(loginName)) {
            System.out.println(loginName + " " + account.getLoginStatus());
            if (!account.getLoginStatus()) {
                UUID loginSessionId = UUID.randomUUID();
                this.getMapAccount().put(loginName, account
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

    @RequestMapping(value = "/count/{id}")
    public String count(@PathVariable("id") UUID id) {
        if (mapSessionGuess.containsKey(id)) {
            return mapSessionGuess.get(id).getCountTry().toString();
        }
        return "Session does not exist";
    }

    @RequestMapping(value = "/guess/{gameSessionId}/{number}")
    public RegisterResponseGuess guess(@PathVariable("gameSessionId") UUID gameSessionId,
                                       @PathVariable("number") Integer number) {

        Account account = getMapAccount().values().stream().filter(a -> a.getSessionIdGuess() != null)
                .filter(a -> a.getSessionIdGuess().equals(gameSessionId))
                .findAny().orElseGet(null);
        if (mapSessionGuess.containsKey(gameSessionId)) {
            SessionGuess session = mapSessionGuess.get(gameSessionId);
            mapSessionGuess.put(gameSessionId, session.
                    withCountTry(session.getCountTry() + 1));
            if (session.getRandomNumber() > number) {
                return new RegisterResponseGuess(">", mapSessionGuess.get(gameSessionId).getCountTry(),
                        "Число больше.");
            } else if (session.getRandomNumber() < number) {
                return new RegisterResponseGuess("<", mapSessionGuess.get(gameSessionId).getCountTry(),
                        "Число меньше.");
            } else {
                getMapAccount().put(account.getLoginName(), account.withGameStatus(false));
                Integer count = session.getCountTry();
                List<ResultGuess> resultGuesses = new ArrayList<>(mapHistoryGuess.get(account.getLoginName()));
                resultGuesses.set(resultGuesses.size() - 1, new ResultGuess(account.getLoginName(), account.getSessionIdGuess(), count, true));
                mapHistoryGuess.put(account.getLoginName(), resultGuesses);
                terminate(gameSessionId);
                return new RegisterResponseGuess("=", count, "Угадал за " + count.toString() + ".");
            }
        } else {
            return new RegisterResponseGuess(null, null, "Session does not exist");
        }
    }

    @RequestMapping(value = "/users")
    public List<Account> users() {
        return new ArrayList<>(getMapAccount().values());
    }

    @RequestMapping(value = "/users/{userName}/history")
    public String historyGames(@PathVariable("userName") String userName) {
        if (mapHistoryGuess.containsKey(userName)) {
            Integer countGames = mapHistoryGuess.get(userName).size();
            return countGames.toString() + " игры сыграно. Выберите нужную вам игру.";
        } else {
            return "Логин не найден.";
        }
    }

    @RequestMapping(value = "/users/{userName}/history/{numberGame}")
    public String resultGame(@PathVariable("userName") String userName, @PathVariable("numberGame") int numberGame) {
        if (mapHistoryGuess.containsKey(userName)) {
            if (mapHistoryGuess.get(userName).size() >= numberGame && numberGame > 0) {
                String result = "Сделано " + mapHistoryGuess.get(userName).get(numberGame - 1).getAttempts().toString() + " попыток.";
                if (mapHistoryGuess.get(userName).get(numberGame - 1).getWin()) {
                    return result + " Победа!";
                } else {
                    return result + " Игра прервана.";
                }
            } else {
                return "Игры под таким номером нет.";
            }
        } else {
            return "Логин не найден.";
        }
    }

    @RequestMapping(value = "/terminate/{id}")
    public String terminate(@PathVariable("id") UUID id) {
        if (mapSessionGuess.containsKey(id)) {
            mapSessionGuess.remove(id);
            return "Session terminate";
        }
        return "Session does not exist";
    }

    @RequestMapping(value = "/logout/{loginName}")
    public logoutResponse logout(@PathVariable("loginName") String loginName) {
        System.out.println("Выход.");
        Account account = getMapAccount().get(loginName);
        if (getMapAccount().containsKey(loginName)) {
            if (account.getLoginStatus()) {
                if (account.getGameStatus()) {
                    List<ResultGuess> resultGuesses = new ArrayList<>(mapHistoryGuess.get(loginName));
                    resultGuesses.set(resultGuesses.size() - 1,
                            new ResultGuess(loginName, account.getSessionIdGuess(),
                                    mapSessionGuess.get(account.getSessionIdGuess()).getCountTry(),
                                    resultGuesses.get(resultGuesses.size() - 1).getWin()));
                    mapHistoryGuess.put(loginName, resultGuesses);
                    terminate(account.getSessionIdGuess());
                    this.getMapAccount().put(loginName, account
                            .withGameStatus(false)
                            .withSessionIdGuess(null)
                            .withLoginStatus(false)
                            .withLoginSessionId(null)
                    );
                    System.out.println("Вышло 1");
                    return new logoutResponse(true, "Игра прервана. Увидимся вновь!");
                } else {
                    System.out.println("Вышло 2");
                    getMapAccount().put(loginName, account.withLoginStatus(false));
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

//    -----------------------------------------------------------------------------------------------------------------------
//    -----------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/start_tic_tac_toe/{nameFirstPlayer}/{nameSecondPlayer}")
    public RegisterResponse startTicTacToe(@PathVariable("nameFirstPlayer") String nameFirstPlayer,
                                           @PathVariable("nameSecondPlayer") String nameSecondPlayer) {
        RegisterResponse registerResponse;
        if (getMapAccount().containsKey(nameFirstPlayer) && getMapAccount().containsKey(nameSecondPlayer)) {
            Account firstAccount = getMapAccount().get(nameFirstPlayer);
            Account secondAccount = getMapAccount().get(nameSecondPlayer);

            if (!firstAccount.getGameStatus() && !secondAccount.getGameStatus()) {
                UUID gameSessionId = UUID.randomUUID();

                mapSessionTicTacToe.put(gameSessionId,
                        new SessionTicTacToe(gameSessionId, nameFirstPlayer, nameSecondPlayer, true));
                getMapAccount().put(nameFirstPlayer
                        , new Account(
                                firstAccount.getLoginName(), firstAccount.getLoginSessionId()
                                , firstAccount.getLoginStatus(), true,
                                firstAccount.getSessionIdGuess(), gameSessionId
                        )
                );

                List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(firstAccount.getLoginName()));
                resultTicTacToe.add(new ResultTicTacToe(gameSessionId, firstAccount.getLoginName(),
                        secondAccount.getLoginName(), "", ""));
                mapHistoryTicTacToe.put(firstAccount.getLoginName(), resultTicTacToe);

                mapSessionTicTacToe.put(gameSessionId,
                        new SessionTicTacToe(gameSessionId, nameFirstPlayer, nameSecondPlayer, true));
                getMapAccount().put(nameSecondPlayer,
                        new Account(
                                secondAccount.getLoginName(), secondAccount.getLoginSessionId()
                                , secondAccount.getLoginStatus(), true,
                                secondAccount.getSessionIdGuess(), gameSessionId
                        )
                );

                resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(secondAccount.getLoginName()));
                resultTicTacToe.add(new ResultTicTacToe(gameSessionId, firstAccount.getLoginName(),
                        secondAccount.getLoginName(), "", ""));
                mapHistoryTicTacToe.put(secondAccount.getLoginName(), resultTicTacToe);
                registerResponse = new RegisterResponse(gameSessionId, "");
                newGame();

                return registerResponse;
            } else if (getMapAccount().get(nameFirstPlayer).getGameStatus() && !getMapAccount().get(nameSecondPlayer).getGameStatus()) {
                registerResponse = new RegisterResponse(null, "Сначала закончите предыдущую игру на первом аккаунте!");
                return registerResponse;
            } else if (!getMapAccount().get(nameFirstPlayer).getGameStatus() && getMapAccount().get(nameSecondPlayer).getGameStatus()) {
                registerResponse = new RegisterResponse(null, "Сначала закончите предыдущую игру на втором аккаунте аккаунте!");
                return registerResponse;
            } else {
                registerResponse = new RegisterResponse(null, "Сначала закончите предыдущие игры на обоих аккаунтах!");
                return registerResponse;
            }
        } else if (!getMapAccount().containsKey(nameFirstPlayer) && getMapAccount().containsKey(nameSecondPlayer)) {
            registerResponse = new RegisterResponse(null, "Первый аккаунт не залогинен.");
            return registerResponse;
        } else if (getMapAccount().containsKey(nameFirstPlayer) && !getMapAccount().containsKey(nameSecondPlayer)) {
            registerResponse = new RegisterResponse(null, "Второй аккаунт не залогинен.");
            return registerResponse;
        }
        registerResponse = new RegisterResponse(null, "Нужно сначала залогиниться с обоих аккаунтов.");
        return registerResponse;
    }

    //    /*
    @RequestMapping(value = "/tic_tac_toe/{sessionIdTicTacToe}/{login}/{x}/{y}")

    public List<List<String>> game(@PathVariable("sessionIdTicTacToe") UUID sessionIdTicTacToe,
                                   @PathVariable("login") String login,
                                   @PathVariable("x") int x,
                                   @PathVariable("y") int y) {
        if (!mapSessionTicTacToe.containsKey(sessionIdTicTacToe)) {
//            return new RegisterResponseGuess(null, null, "Session does not exist");
            return null;
        }
        if (!accountService.getMapAccount().containsKey(login)) {
            table.get(3).set(0, "Такого аккаунта нет!");
            return table;
//                "Такого аккаунта нет!"
        }
        Account account = accountService.getMapAccount().get(login);
        if (!account.getSessionIdTicTacToe().equals(sessionIdTicTacToe)) {
            table.get(3).set(0, "В ткущей сессии этот игрок не участвует.");
            return table;
//                    "В ткущей сессии этот игрок не участвует."
        }
        String endMessage = mapHistoryTicTacToe.get(login).get(mapHistoryTicTacToe.get(login).size() - 1).getEndMessage();
        if (endMessage.equals("Выиграли крестики.") ||
                table.get(3).get(0).equals("Выиграли нолики.") ||
                table.get(3).get(0).equals("Ничья.")) {
            resultGame.add(table.get(3).get(0));
            return table;
        }
        table.get(3).set(0, "");
        if (x <= 0 || x >= 4 || y <= 0 || y >= 4) {
            table.get(3).set(0, "Таких координат не существует. (поле 3 на 3 клетки).");
            return table;
        }
        if (!checkPaste(x, y)) {
            table.get(3).set(0, "Эта клетка занята. Введите другие координаты.");
            return table;
        }
        if (mapSessionTicTacToe.get(sessionIdTicTacToe).getCurrentMove()) {
            table.get(x - 1).set(y - 1, "x");

        } else {
            table.get(x - 1).set(y - 1, "o");
        }
        winner(mapSessionTicTacToe.get(sessionIdTicTacToe).getCurrentMove(),
                mapSessionTicTacToe.get(sessionIdTicTacToe).getNameFirstPlayer(),
                mapSessionTicTacToe.get(sessionIdTicTacToe).getNameSecondPlayer());
        mapSessionTicTacToe.put(sessionIdTicTacToe, mapSessionTicTacToe.get(sessionIdTicTacToe).
                withCurrentMove(!mapSessionTicTacToe.get(sessionIdTicTacToe).getCurrentMove()));
//                            symbol = !symbol;
        String firstPlayer = mapHistoryTicTacToe.get(login).get(mapHistoryTicTacToe.get(login).size() - 1).getNameFirstPlayer();
        String secondPlayer = mapHistoryTicTacToe.get(login).get(mapHistoryTicTacToe.get(login).size() - 1).getNameSecondPlayer();

//        List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(firstPlayer));
//        ResultTicTacToe currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
//        resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
//                firstPlayer,
//                secondPlayer,
//                "",
//                ""));
//        mapHistoryTicTacToe.put(firstPlayer, resultTicTacToe);
        return table;
    }/**/

    @RequestMapping(value = "/tic_tac_toe/users")
    public List<Account> usersTicTacToe() {
        return new ArrayList<>(getMapAccount().values());
    }

    @RequestMapping(value = "/tic_tac_toe/users/{userName}/history")
    public String historyTicTacToe(@PathVariable("userName") String userName) {
        if (mapHistoryTicTacToe.containsKey(userName)) {
            Integer countGames = mapHistoryTicTacToe.get(userName).size();
            return countGames.toString() + " игры сыграно. Выберите нужную вам игру.";
        } else {
            return "Логин не найден.";
        }
    }

    @RequestMapping(value = "/tic_tac_toe/users/{userName}/history/{numberGame}")
    public String resultTicTacToe(@PathVariable("userName") String userName, @PathVariable("numberGame") int numberGame) {
        if (mapHistoryTicTacToe.containsKey(userName)) {
            if (mapHistoryTicTacToe.get(userName).size() >= numberGame && numberGame > 0) {
                String result = "Играл " + mapHistoryTicTacToe.get(userName).get(numberGame - 1).getNameFirstPlayer() + " c "
                        + mapHistoryTicTacToe.get(userName).get(numberGame - 1).getNameSecondPlayer() + ".";
//                if (mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner().
//                        equals(mapHistoryTicTacToe.get(userName).get(numberGame - 1).getNameFirstPlayer())) {
                    return result + " Победил " + mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner() + ". " +
                            mapHistoryTicTacToe.get(userName).get(numberGame - 1).getEndMessage();
//                } else if (mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner().
//                        equals(mapHistoryTicTacToe.get(userName).get(numberGame - 1).getNameSecondPlayer())){
//                    return result + " Победил " + mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner() + ".";
//                } else {
//                    return result + " " + mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner() + " " +
//                            mapHistoryTicTacToe.get(userName).get(numberGame - 1).getEndMessage();
//                }
            } else {
                return "Игры под таким номером нет.";
            }
        } else {
            return "Логин не найден.";
        }
    }

    public void fill() {
        table = new ArrayList<>(3);
        table.add(new ArrayList<>(List.of(" ", " ", " ")));
        table.add(new ArrayList<>(List.of(" ", " ", " ")));
        table.add(new ArrayList<>(List.of(" ", " ", " ")));
        table.add(new ArrayList<>(List.of("")));
    }

    @RequestMapping(value = "/play")
    public List<List<String>> newGame() {
        fill();
        return table;
    }

    public boolean checkPaste(int x, int y) {
        if (table.get(x - 1).get(y - 1).equals(" ")) {
            return true;
        }
        return false;
    }

    public void winner(boolean ticTacToeSymbol, String firstPlayer, String secondPlayer) {
        String symbol;
        String winner;
        if (ticTacToeSymbol) {
            symbol = "x";
            winner = firstPlayer;
        } else {
            symbol = "o";
            winner = secondPlayer;
        }
        if (table.get(0).get(0).equals(symbol) &&
                table.get(0).get(1).equals(symbol) &&
                table.get(0).get(2).equals(symbol) ||
                table.get(1).get(0).equals(symbol) &&
                        table.get(1).get(1).equals(symbol) &&
                        table.get(1).get(2).equals(symbol) ||
                table.get(2).get(0).equals(symbol) &&
                        table.get(2).get(1).equals(symbol) &&
                        table.get(2).get(2).equals(symbol) ||
                table.get(0).get(0).equals(symbol) &&
                        table.get(1).get(0).equals(symbol) &&
                        table.get(2).get(0).equals(symbol) ||
                table.get(0).get(1).equals(symbol) &&
                        table.get(1).get(1).equals(symbol) &&
                        table.get(2).get(1).equals(symbol) ||
                table.get(0).get(2).equals(symbol) &&
                        table.get(1).get(2).equals(symbol) &&
                        table.get(2).get(2).equals(symbol) ||
                table.get(0).get(0).equals(symbol) &&
                        table.get(1).get(1).equals(symbol) &&
                        table.get(2).get(2).equals(symbol) ||
                table.get(0).get(2).equals(symbol) &&
                        table.get(1).get(1).equals(symbol) &&
                        table.get(2).get(0).equals(symbol)) {
            if (symbol.equals("x")) {
                List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(firstPlayer));
                ResultTicTacToe currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
                resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
                        firstPlayer,
                        secondPlayer,
                        winner,
                        "Выиграли крестики."));
                mapHistoryTicTacToe.put(firstPlayer, resultTicTacToe);
                resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(secondPlayer));
                currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
                resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
                        firstPlayer,
                        secondPlayer,
                        winner,
                        "Выиграли крестики."));
                mapHistoryTicTacToe.put(secondPlayer, resultTicTacToe);

                table.get(3).set(0, "Выиграли крестики.");
                resultGame.add(table.get(3).get(0));
                countGame++;
//                return "Выиграли крестики.";
            } else {
                List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(firstPlayer));
                ResultTicTacToe currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
                resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
                        firstPlayer,
                        secondPlayer,
                        winner,
                        "Выиграли нолики."));
                mapHistoryTicTacToe.put(firstPlayer, resultTicTacToe);
                resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(secondPlayer));
                currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
                resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
                        firstPlayer,
                        secondPlayer,
                        winner,
                        "Выиграли нолики."));
                mapHistoryTicTacToe.put(secondPlayer, resultTicTacToe);

                table.get(3).set(0, "Выиграли нолики.");
                resultGame.add(table.get(3).get(0));
                countGame++;
//                return "Выиграли нолики.";
            }
        }
        boolean fullness = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                if (table.get(i).get(j).equals(" ")) {
                    fullness = false;
                }
            }
        }
        if (fullness) {
            winner = "Ничья";
            List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(firstPlayer));
            ResultTicTacToe currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
            resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
                    firstPlayer,
                    secondPlayer,
                    winner,
                    "Ничья."));
            resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(secondPlayer));
            currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
            resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
                    firstPlayer,
                    secondPlayer,
                    winner,
                    "Ничья."));
            table.get(3).set(0, "Ничья.");
            resultGame.add(table.get(3).get(0));
            countGame++;
//            return "Ничья";
        }
//        return "";
    }

}
