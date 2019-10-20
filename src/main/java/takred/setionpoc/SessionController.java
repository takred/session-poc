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

    @RequestMapping(value = "/start/{loginSessionId}")
    public String start(@PathVariable("loginSessionId") UUID loginSessionId) {
        Account account = mapAccount.values().stream().filter(a -> a.getLoginSessionId().equals(loginSessionId))
                .findAny().orElseGet(null);
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
                return gameSessionId.toString();
            } else{
                return "Сначала закончите предыдущую игру!";
            }
        }
        return "Нужно сначала залогиниться.";
    }

    @RequestMapping(value = "/register/{loginName}")
    public String register(@PathVariable("loginName") String loginName) {
        if (!mapAccount.containsKey(loginName)) {
            UUID loginSessionId = UUID.randomUUID();
            mapAccount.put(loginName, new Account(loginName, loginSessionId,
                    true, false, null));
            return "Добро пожаловать.";
        }
        return "Логин уже сущетвует.";
    }

    @RequestMapping(value = "/login/{loginName}")
    public String login(@PathVariable("loginName") String loginName) {
        Account loginNameObj = mapAccount.get(loginName);
        if (mapAccount.containsKey(loginName)) {
            if (!loginNameObj.getLoginStatus()) {
                UUID loginSessionId = UUID.randomUUID();
                this.mapAccount.put(loginName, loginNameObj
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
            return mapSession.get(id).getCountLogin().toString();
        }
        return "Session does not exist";
    }

    @RequestMapping(value = "/guess/{gameSessionId}/{number}")
    public String guess(@PathVariable("gameSessionId") UUID gameSessionId, @PathVariable("number") Integer number) {
        if (mapSession.containsKey(gameSessionId)) {
            Session gameSessionIdObj = mapSession.get(gameSessionId);
            mapSession.put(gameSessionId, gameSessionIdObj.
                    setCountLogin(gameSessionIdObj.getCountLogin() + 1));
            if (gameSessionIdObj.getRandomNumber() > number) {
                return "Число больше.";
            } else if (gameSessionIdObj.getRandomNumber() < number) {
                return "Число меньше.";
            } else {
                Integer count = gameSessionIdObj.getCountLogin();
                terminate(gameSessionId);
                return "Угадал за " + count.toString() + ".";
            }
        } else {
            return "Session does not exist";
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
        Account loginNameObj = mapAccount.get(loginName);
        if (mapAccount.containsKey(loginName)) {
            if (loginNameObj.getLoginStatus()) {
                if (loginNameObj.getGameStatus()) {
                    terminate(loginNameObj.getGameSessionId());
                    this.mapAccount.put(loginName, loginNameObj
                            .setGameStatus(false)
                            .setGameSessionId(null)
                            .setLoginStatus(false)
                            .setLoginSessionId(null)
                    );
                   return  "Игра прервана. Увидимся вновь!";
                } else {
                    mapAccount.put(loginName, loginNameObj.setLoginStatus(false));
                    return "До свидания!";
                }
            } else {
                return "Сначала надо войти.";
            }
        }
        return "Такого логина не существует.";
    }
}
