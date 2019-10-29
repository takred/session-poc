package takred.setionpoc;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/")
public class SessionController {
    private Map<UUID, Session> mapSession = new HashMap<>();
    private Map<String, Account> mapAccount = new HashMap<>();
    private Map<String, List<ResultGame>> mapHistoryGames = new HashMap<>();

    @RequestMapping(value = "/start/{loginSessionId}")
    public RegisterResponse start(@PathVariable("loginSessionId") UUID loginSessionId) {
        Account account = mapAccount.values().stream().filter(a -> a.getLoginSessionId().equals(loginSessionId))
                .findAny().orElseGet(null);
        RegisterResponse registerResponse;
        if (account != null) {
            if (!mapAccount.get(account.getLoginName()).getGameStatus()) {
                UUID gameSessionId = UUID.randomUUID();
                mapSession.put(gameSessionId
                        , new Session(
                                gameSessionId, 0
                                , ThreadLocalRandom.current().nextInt(0, 10000)
                        )
                );
                mapAccount.put(account.getLoginName()
                        , new Account(
                                account.getLoginName(), account.getLoginSessionId()
                                , account.getLoginStatus(), true, gameSessionId
                        )
                );
                List<ResultGame> resultGames = new ArrayList<>(mapHistoryGames.get(account.getLoginName()));
                resultGames.add(new ResultGame(account.getLoginName(), gameSessionId, 0, false));
                mapHistoryGames.put(account.getLoginName(), resultGames);
                registerResponse = new RegisterResponse(gameSessionId, "");
                return registerResponse;
            } else{
                registerResponse = new RegisterResponse(null, "Сначала закончите предыдущую игру!");
                return registerResponse;
            }
        }
        registerResponse = new RegisterResponse(null, "Нужно сначала залогиниться.");
        return registerResponse;
    }

    @RequestMapping(value = "/register/{loginName}")
    public RegisterResponse register(@PathVariable("loginName") String loginName) {
        if (!mapAccount.containsKey(loginName)) {
            UUID loginSessionId = UUID.randomUUID();
            mapAccount.put(loginName, new Account(loginName, loginSessionId,
                    true, false, null));
            mapHistoryGames.put(loginName, new ArrayList<>());
            return new RegisterResponse(loginSessionId, "Добро пожаловать. ");
        }
        return new RegisterResponse(null, "Логин уже сущетвует.");
    }

    @RequestMapping(value = "/login/{loginName}")
    public String login(@PathVariable("loginName") String loginName) {
        Account account = mapAccount.get(loginName);
        if (mapAccount.containsKey(loginName)) {
            if (!account.getLoginStatus()) {
                UUID loginSessionId = UUID.randomUUID();
                this.mapAccount.put(loginName, account
                        .setLoginSessionId(loginSessionId)
                        .setLoginStatus(true)
                        .setGameStatus(false)
                );
                return loginSessionId.toString();
            } else {
                return "Нужно сначала разлогиниться.";
            }
        }
        return "Логин не найден.";
    }

    @RequestMapping(value = "/count/{id}")
    public String count(@PathVariable("id") UUID id) {
        if (mapSession.containsKey(id)) {
            return mapSession.get(id).getCountTry().toString();
        }
        return "Session does not exist";
    }

    @RequestMapping(value = "/guess/{gameSessionId}/{number}")
    public RegisterResponseGuess guess(@PathVariable("gameSessionId") UUID gameSessionId, @PathVariable("number") Integer number) {
        Account account = mapAccount.values().stream().filter(a -> a.getGameSessionId().equals(gameSessionId)).findAny().orElseGet(null);
        if (mapSession.containsKey(gameSessionId)) {
            Session session = mapSession.get(gameSessionId);
            mapSession.put(gameSessionId, session.
                    setCountTry(session.getCountTry() + 1));
            if (session.getRandomNumber() > number) {
                return new RegisterResponseGuess(">", mapSession.get(gameSessionId).getCountTry(), "Число больше.");
            } else if (session.getRandomNumber() < number) {
                return new RegisterResponseGuess("<", mapSession.get(gameSessionId).getCountTry(), "Число меньше.");
            } else {
                mapAccount.put(account.getLoginName(),account.setGameStatus(false));
                Integer count = session.getCountTry();
                List<ResultGame> resultGames = new ArrayList<>(mapHistoryGames.get(account.getLoginName()));
                resultGames.set(resultGames.size() - 1, mapHistoryGames.get(account.getLoginName()).get(resultGames.size() - 1).setAttempts(count));
                mapHistoryGames.put(account.getLoginName(), resultGames);
                terminate(gameSessionId);
                return new RegisterResponseGuess("=", count, "Угадал за " + count.toString() + ".");
            }
        } else {
            return new RegisterResponseGuess(null, null, "Session does not exist");
        }
    }

    @RequestMapping(value = "/historyGames/{loginName}")
    public String historyGames(@PathVariable("loginName") String loginName){
        if (mapHistoryGames.containsKey(loginName)) {
            Integer countGames = mapHistoryGames.get(loginName).size();
            return countGames.toString() + " игры сыграно. Выберите нужную вам игру.";
        } else {
            return "Логин не найден.";
        }
    }

    @RequestMapping(value = "/historyGames/{loginName}/{numberGame}")
    public String resultGame(@PathVariable("loginName") String loginName, @PathVariable("numberGame") int numberGame){
        if (mapHistoryGames.containsKey(loginName)) {
            if (mapHistoryGames.get(loginName).size() >= numberGame && numberGame > 0){
                String result = "Сделано " + mapHistoryGames.get(loginName).get(numberGame - 1).getAttempts().toString() + " попыток.";
                if (mapHistoryGames.get(loginName).get(numberGame - 1).getWin()){
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
        if (mapSession.containsKey(id)) {
            mapSession.remove(id);
            return "Session terminate";
        }
        return "Session does not exist";
    }

    @RequestMapping(value = "/logout/{loginName}")
    public String logout(@PathVariable("loginName") String loginName){
        Account account = mapAccount.get(loginName);
        if (mapAccount.containsKey(loginName)) {
            if (account.getLoginStatus()) {
                if (account.getGameStatus()) {
                    List<ResultGame> resultGames = new ArrayList<>(mapHistoryGames.get(loginName));
                    resultGames.set(resultGames.size() - 1,
                            new ResultGame(loginName, account.getGameSessionId(),
                                    mapSession.get(account.getGameSessionId()).getCountTry(),
                                    resultGames.get(resultGames.size() - 1).getWin()));
                    mapHistoryGames.put(loginName, resultGames);
                    terminate(account.getGameSessionId());
                    this.mapAccount.put(loginName, account
                            .setGameStatus(false)
                            .setGameSessionId(null)
                            .setLoginStatus(false)
                            .setLoginSessionId(null)
                    );
                   return  "Игра прервана. Увидимся вновь!";
                } else {
                    mapAccount.put(loginName, account.setLoginStatus(false));
                    return "До свидания!";
                }
            } else {
                return "Сначала надо войти.";
            }
        }
        return "Такого логина не существует.";
    }
}
