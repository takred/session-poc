package takred.setionpoc.guess;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import takred.setionpoc.*;

import java.util.*;

@RestController
@RequestMapping(value = "/guess")
public class GuessController {
    private final AccountService accountService;
    private final GuessService guessService;

    public GuessController(AccountService accountService, GuessService guessService) {
        this.accountService = accountService;
        this.guessService = guessService;
    }
    private Map<UUID, SessionGuess> getMapSessionGuess(){
        return guessService.getMapSessionGuess();
    }

    private Map<String, List<ResultGuess>> getMapHistoryGuess(){
        return guessService.getMapHistoryGuess();
    }

    private Map<String, Account> getMapAccount() {
        return accountService.getMapAccount();
    }

    @RequestMapping(value = "/start/{loginSessionId}")
    public RegisterResponse startGuess(@PathVariable("loginSessionId") UUID loginSessionId) {
        return guessService.startGuess(loginSessionId);
    }

    @RequestMapping(value = "/count/{id}")
    public String count(@PathVariable("id") UUID id) {
        return guessService.count(id);
    }

    @RequestMapping(value = "/play/{gameSessionId}/{number}")
    public RegisterResponseGuess guess(@PathVariable("gameSessionId") UUID gameSessionId,
                                       @PathVariable("number") Integer number) {
        return guessService.guess(gameSessionId, number);
    }

    @RequestMapping(value = "/users")
    public List<Account> users() {
        return guessService.users();
    }

    @RequestMapping(value = "/users/{userName}/history")
    public String historyGames(@PathVariable("userName") String userName) {
        return guessService.historyGames(userName);
    }

    @RequestMapping(value = "/users/{userName}/history/{numberGame}")
    public String resultGame(@PathVariable("userName") String userName, @PathVariable("numberGame") int numberGame) {
        return guessService.resultGame(userName, numberGame);
    }

    @RequestMapping(value = "/terminate/{id}")
    public String terminate(@PathVariable("id") UUID id) {
        if (getMapSessionGuess().containsKey(id)) {
            getMapSessionGuess().remove(id);
            return "Session terminate";
        }
        return "Session does not exist";
    }
//    -----------------------------------------------------------------------------------------------------------------------
//    -----------------------------------------------------------------------------------------------------------------------

//    @RequestMapping(value = "/start_tic_tac_toe/{nameFirstPlayer}/{nameSecondPlayer}")
//    public RegisterResponse startTicTacToe(@PathVariable("nameFirstPlayer") String nameFirstPlayer,
//                                           @PathVariable("nameSecondPlayer") String nameSecondPlayer) {
//        RegisterResponse registerResponse;
//        if (getMapAccount().containsKey(nameFirstPlayer) && getMapAccount().containsKey(nameSecondPlayer)) {
//            Account firstAccount = getMapAccount().get(nameFirstPlayer);
//            Account secondAccount = getMapAccount().get(nameSecondPlayer);
//
//            if (!firstAccount.getGameStatus() && !secondAccount.getGameStatus()) {
//                UUID gameSessionId = UUID.randomUUID();
//
//                getMapSessionTicTacToe().put(gameSessionId,
//                        new SessionTicTacToe(gameSessionId, nameFirstPlayer, nameSecondPlayer, true));
//                getMapAccount().put(nameFirstPlayer
//                        , new Account(
//                                firstAccount.getLoginName(), firstAccount.getLoginSessionId()
//                                , firstAccount.getLoginStatus(), true,
//                                firstAccount.getSessionIdGuess(), gameSessionId
//                        )
//                );
//
//                List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(getMapHistoryTicTacToe().get(firstAccount.getLoginName()));
//                resultTicTacToe.add(new ResultTicTacToe(gameSessionId, firstAccount.getLoginName(),
//                        secondAccount.getLoginName(), "", ""));
//                getMapHistoryTicTacToe().put(firstAccount.getLoginName(), resultTicTacToe);
//
//                getMapSessionTicTacToe().put(gameSessionId,
//                        new SessionTicTacToe(gameSessionId, nameFirstPlayer, nameSecondPlayer, true));
//                getMapAccount().put(nameSecondPlayer,
//                        new Account(
//                                secondAccount.getLoginName(), secondAccount.getLoginSessionId()
//                                , secondAccount.getLoginStatus(), true,
//                                secondAccount.getSessionIdGuess(), gameSessionId
//                        )
//                );
//
//                resultTicTacToe = new ArrayList<>(getMapHistoryTicTacToe().get(secondAccount.getLoginName()));
//                resultTicTacToe.add(new ResultTicTacToe(gameSessionId, firstAccount.getLoginName(),
//                        secondAccount.getLoginName(), "", ""));
//                getMapHistoryTicTacToe().put(secondAccount.getLoginName(), resultTicTacToe);
//                registerResponse = new RegisterResponse(gameSessionId, "");
//                newGame();
//
//                return registerResponse;
//            } else if (getMapAccount().get(nameFirstPlayer).getGameStatus() && !getMapAccount().get(nameSecondPlayer).getGameStatus()) {
//                registerResponse = new RegisterResponse(null, "Сначала закончите предыдущую игру на первом аккаунте!");
//                return registerResponse;
//            } else if (!getMapAccount().get(nameFirstPlayer).getGameStatus() && getMapAccount().get(nameSecondPlayer).getGameStatus()) {
//                registerResponse = new RegisterResponse(null, "Сначала закончите предыдущую игру на втором аккаунте аккаунте!");
//                return registerResponse;
//            } else {
//                registerResponse = new RegisterResponse(null, "Сначала закончите предыдущие игры на обоих аккаунтах!");
//                return registerResponse;
//            }
//        } else if (!getMapAccount().containsKey(nameFirstPlayer) && getMapAccount().containsKey(nameSecondPlayer)) {
//            registerResponse = new RegisterResponse(null, "Первый аккаунт не залогинен.");
//            return registerResponse;
//        } else if (getMapAccount().containsKey(nameFirstPlayer) && !getMapAccount().containsKey(nameSecondPlayer)) {
//            registerResponse = new RegisterResponse(null, "Второй аккаунт не залогинен.");
//            return registerResponse;
//        }
//        registerResponse = new RegisterResponse(null, "Нужно сначала залогиниться с обоих аккаунтов.");
//        return registerResponse;
//    }
//
//    //    /*
//    @RequestMapping(value = "/tic_tac_toe/{sessionIdTicTacToe}/{login}/{x}/{y}")
//
//    public List<List<String>> game(@PathVariable("sessionIdTicTacToe") UUID sessionIdTicTacToe,
//                                   @PathVariable("login") String login,
//                                   @PathVariable("x") int x,
//                                   @PathVariable("y") int y) {
//        if (!getMapSessionTicTacToe().containsKey(sessionIdTicTacToe)) {
////            return new RegisterResponseGuess(null, null, "Session does not exist");
//            return null;
//        }
//        if (!accountService.getMapAccount().containsKey(login)) {
//            table.get(3).set(0, "Такого аккаунта нет!");
//            return table;
////                "Такого аккаунта нет!"
//        }
//        Account account = accountService.getMapAccount().get(login);
//        if (!account.getSessionIdTicTacToe().equals(sessionIdTicTacToe)) {
//            table.get(3).set(0, "В ткущей сессии этот игрок не участвует.");
//            return table;
////                    "В ткущей сессии этот игрок не участвует."
//        }
//        String endMessage = getMapHistoryTicTacToe().get(login).get(getMapHistoryTicTacToe().get(login).size() - 1).getEndMessage();
//        if (endMessage.equals("Выиграли крестики.") ||
//                table.get(3).get(0).equals("Выиграли нолики.") ||
//                table.get(3).get(0).equals("Ничья.")) {
//            resultGame.add(table.get(3).get(0));
//            return table;
//        }
//        table.get(3).set(0, "");
//        if (x <= 0 || x >= 4 || y <= 0 || y >= 4) {
//            table.get(3).set(0, "Таких координат не существует. (поле 3 на 3 клетки).");
//            return table;
//        }
//        if (!checkPaste(x, y)) {
//            table.get(3).set(0, "Эта клетка занята. Введите другие координаты.");
//            return table;
//        }
//        if (getMapSessionTicTacToe().get(sessionIdTicTacToe).getCurrentMove()) {
//            table.get(x - 1).set(y - 1, "x");
//
//        } else {
//            table.get(x - 1).set(y - 1, "o");
//        }
//        winner(getMapSessionTicTacToe().get(sessionIdTicTacToe).getCurrentMove(),
//                getMapSessionTicTacToe().get(sessionIdTicTacToe).getNameFirstPlayer(),
//                getMapSessionTicTacToe().get(sessionIdTicTacToe).getNameSecondPlayer());
//        getMapSessionTicTacToe().put(sessionIdTicTacToe, getMapSessionTicTacToe().get(sessionIdTicTacToe).
//                withCurrentMove(!getMapSessionTicTacToe().get(sessionIdTicTacToe).getCurrentMove()));
////                            symbol = !symbol;
//        String firstPlayer = getMapHistoryTicTacToe().get(login).get(getMapHistoryTicTacToe().get(login).size() - 1).getNameFirstPlayer();
//        String secondPlayer = getMapHistoryTicTacToe().get(login).get(getMapHistoryTicTacToe().get(login).size() - 1).getNameSecondPlayer();
//
////        List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(mapHistoryTicTacToe.get(firstPlayer));
////        ResultTicTacToe currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
////        resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
////                firstPlayer,
////                secondPlayer,
////                "",
////                ""));
////        mapHistoryTicTacToe.put(firstPlayer, resultTicTacToe);
//        return table;
//    }/**/
//
//    @RequestMapping(value = "/tic_tac_toe/users")
//    public List<Account> usersTicTacToe() {
//        return new ArrayList<>(getMapAccount().values());
//    }
//
//    @RequestMapping(value = "/tic_tac_toe/users/{userName}/history")
//    public String historyTicTacToe(@PathVariable("userName") String userName) {
//        if (getMapHistoryTicTacToe().containsKey(userName)) {
//            Integer countGames = getMapHistoryTicTacToe().get(userName).size();
//            return countGames.toString() + " игры сыграно. Выберите нужную вам игру.";
//        } else {
//            return "Логин не найден.";
//        }
//    }
//
//    @RequestMapping(value = "/tic_tac_toe/users/{userName}/history/{numberGame}")
//    public String resultTicTacToe(@PathVariable("userName") String userName, @PathVariable("numberGame") int numberGame) {
//        if (getMapHistoryTicTacToe().containsKey(userName)) {
//            if (getMapHistoryTicTacToe().get(userName).size() >= numberGame && numberGame > 0) {
//                String result = "Играл " + getMapHistoryTicTacToe().get(userName).get(numberGame - 1).getNameFirstPlayer() + " c "
//                        + getMapHistoryTicTacToe().get(userName).get(numberGame - 1).getNameSecondPlayer() + ".";
////                if (mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner().
////                        equals(mapHistoryTicTacToe.get(userName).get(numberGame - 1).getNameFirstPlayer())) {
//                    return result + " Победил " + getMapHistoryTicTacToe().get(userName).get(numberGame - 1).getWinner() + ". " +
//                            getMapHistoryTicTacToe().get(userName).get(numberGame - 1).getEndMessage();
////                } else if (mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner().
////                        equals(mapHistoryTicTacToe.get(userName).get(numberGame - 1).getNameSecondPlayer())){
////                    return result + " Победил " + mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner() + ".";
////                } else {
////                    return result + " " + mapHistoryTicTacToe.get(userName).get(numberGame - 1).getWinner() + " " +
////                            mapHistoryTicTacToe.get(userName).get(numberGame - 1).getEndMessage();
////                }
//            } else {
//                return "Игры под таким номером нет.";
//            }
//        } else {
//            return "Логин не найден.";
//        }
//    }
//
//    public void fill() {
//        table = new ArrayList<>(3);
//        table.add(new ArrayList<>(List.of(" ", " ", " ")));
//        table.add(new ArrayList<>(List.of(" ", " ", " ")));
//        table.add(new ArrayList<>(List.of(" ", " ", " ")));
//        table.add(new ArrayList<>(List.of("")));
//    }
//
//    @RequestMapping(value = "/play")
//    public List<List<String>> newGame() {
//        fill();
//        return table;
//    }
//
//    public boolean checkPaste(int x, int y) {
//        if (table.get(x - 1).get(y - 1).equals(" ")) {
//            return true;
//        }
//        return false;
//    }
//
//    public void winner(boolean ticTacToeSymbol, String firstPlayer, String secondPlayer) {
//        String symbol;
//        String winner;
//        if (ticTacToeSymbol) {
//            symbol = "x";
//            winner = firstPlayer;
//        } else {
//            symbol = "o";
//            winner = secondPlayer;
//        }
//        if (table.get(0).get(0).equals(symbol) &&
//                table.get(0).get(1).equals(symbol) &&
//                table.get(0).get(2).equals(symbol) ||
//                table.get(1).get(0).equals(symbol) &&
//                        table.get(1).get(1).equals(symbol) &&
//                        table.get(1).get(2).equals(symbol) ||
//                table.get(2).get(0).equals(symbol) &&
//                        table.get(2).get(1).equals(symbol) &&
//                        table.get(2).get(2).equals(symbol) ||
//                table.get(0).get(0).equals(symbol) &&
//                        table.get(1).get(0).equals(symbol) &&
//                        table.get(2).get(0).equals(symbol) ||
//                table.get(0).get(1).equals(symbol) &&
//                        table.get(1).get(1).equals(symbol) &&
//                        table.get(2).get(1).equals(symbol) ||
//                table.get(0).get(2).equals(symbol) &&
//                        table.get(1).get(2).equals(symbol) &&
//                        table.get(2).get(2).equals(symbol) ||
//                table.get(0).get(0).equals(symbol) &&
//                        table.get(1).get(1).equals(symbol) &&
//                        table.get(2).get(2).equals(symbol) ||
//                table.get(0).get(2).equals(symbol) &&
//                        table.get(1).get(1).equals(symbol) &&
//                        table.get(2).get(0).equals(symbol)) {
//            if (symbol.equals("x")) {
//                List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(getMapHistoryTicTacToe().get(firstPlayer));
//                ResultTicTacToe currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
//                resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
//                        firstPlayer,
//                        secondPlayer,
//                        winner,
//                        "Выиграли крестики."));
//                getMapHistoryTicTacToe().put(firstPlayer, resultTicTacToe);
//                resultTicTacToe = new ArrayList<>(getMapHistoryTicTacToe().get(secondPlayer));
//                currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
//                resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
//                        firstPlayer,
//                        secondPlayer,
//                        winner,
//                        "Выиграли крестики."));
//                getMapHistoryTicTacToe().put(secondPlayer, resultTicTacToe);
//
//                table.get(3).set(0, "Выиграли крестики.");
//                resultGame.add(table.get(3).get(0));
//                countGame++;
////                return "Выиграли крестики.";
//            } else {
//                List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(getMapHistoryTicTacToe().get(firstPlayer));
//                ResultTicTacToe currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
//                resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
//                        firstPlayer,
//                        secondPlayer,
//                        winner,
//                        "Выиграли нолики."));
//                getMapHistoryTicTacToe().put(firstPlayer, resultTicTacToe);
//                resultTicTacToe = new ArrayList<>(getMapHistoryTicTacToe().get(secondPlayer));
//                currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
//                resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
//                        firstPlayer,
//                        secondPlayer,
//                        winner,
//                        "Выиграли нолики."));
//                getMapHistoryTicTacToe().put(secondPlayer, resultTicTacToe);
//
//                table.get(3).set(0, "Выиграли нолики.");
//                resultGame.add(table.get(3).get(0));
//                countGame++;
////                return "Выиграли нолики.";
//            }
//        }
//        boolean fullness = true;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < table.get(i).size(); j++) {
//                if (table.get(i).get(j).equals(" ")) {
//                    fullness = false;
//                }
//            }
//        }
//        if (fullness) {
//            winner = "Ничья";
//            List<ResultTicTacToe> resultTicTacToe = new ArrayList<>(getMapHistoryTicTacToe().get(firstPlayer));
//            ResultTicTacToe currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
//            resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
//                    firstPlayer,
//                    secondPlayer,
//                    winner,
//                    "Ничья."));
//            resultTicTacToe = new ArrayList<>(getMapHistoryTicTacToe().get(secondPlayer));
//            currentSession = resultTicTacToe.get(resultTicTacToe.size() - 1);
//            resultTicTacToe.set(resultTicTacToe.size() - 1, new ResultTicTacToe(currentSession.getSessionIdTicTacToe(),
//                    firstPlayer,
//                    secondPlayer,
//                    winner,
//                    "Ничья."));
//            table.get(3).set(0, "Ничья.");
//            resultGame.add(table.get(3).get(0));
//            countGame++;
////            return "Ничья";
//        }
////        return "";
//    }

}