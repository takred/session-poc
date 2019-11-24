package takred.setionpoc.guess;

import org.springframework.stereotype.Service;
import takred.setionpoc.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GuessService {
    private Map<UUID, SessionGuess> mapSessionGuess = new HashMap<>();
    private Map<String, List<ResultGuess>> mapHistoryGuess = new HashMap<>();
    private final AccountService accountService;

    public GuessService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Map<UUID, SessionGuess> getMapSessionGuess() {
        return mapSessionGuess;
    }

    public Map<String, List<ResultGuess>> getMapHistoryGuess() {
        return mapHistoryGuess;
    }

    private Map<String, Account> getMapAccount() {
        return accountService.getMapAccount();
    }

    public RegisterResponse startGuess(UUID loginSessionId) {
        Account account = getMapAccount().values().stream()
                .filter(a -> a.getLoginSessionId() != null)
                .filter(a -> a.getLoginSessionId()
                        .equals(loginSessionId))
                .findAny().orElseGet(null);
        RegisterResponse registerResponse;
        if (account != null) {
            if (!getMapAccount().get(account.getLoginName()).getGameStatus()) {
                UUID gameSessionId = UUID.randomUUID();
                getMapSessionGuess().put(gameSessionId
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
                List<ResultGuess> resultGuesses = new ArrayList<>(getMapHistoryGuess().get(account.getLoginName()));
                resultGuesses.add(new ResultGuess(account.getLoginName(), gameSessionId, 0, false));
                getMapHistoryGuess().put(account.getLoginName(), resultGuesses);
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

    public String count(UUID id) {
        if (getMapSessionGuess().containsKey(id)) {
            return getMapSessionGuess().get(id).getCountTry().toString();
        }
        return "Session does not exist";
    }

    public RegisterResponseGuess guess(UUID gameSessionId,
                                       Integer number) {

        Account account = getMapAccount().values().stream().filter(a -> a.getSessionIdGuess() != null)
                .filter(a -> a.getSessionIdGuess().equals(gameSessionId))
                .findAny().orElseGet(null);
        if (getMapSessionGuess().containsKey(gameSessionId)) {
            SessionGuess session = getMapSessionGuess().get(gameSessionId);
            getMapSessionGuess().put(gameSessionId, session.
                    withCountTry(session.getCountTry() + 1));
            if (session.getRandomNumber() > number) {
                return new RegisterResponseGuess(">", getMapSessionGuess().get(gameSessionId).getCountTry(),
                        "Число больше.");
            } else if (session.getRandomNumber() < number) {
                return new RegisterResponseGuess("<", getMapSessionGuess().get(gameSessionId).getCountTry(),
                        "Число меньше.");
            } else {
                getMapAccount().put(account.getLoginName(), account.withGameStatus(false));
                Integer count = session.getCountTry();
                List<ResultGuess> resultGuesses = new ArrayList<>(getMapHistoryGuess().get(account.getLoginName()));
                resultGuesses.set(resultGuesses.size() - 1, new ResultGuess(account.getLoginName(), account.getSessionIdGuess(), count, true));
                getMapHistoryGuess().put(account.getLoginName(), resultGuesses);
                terminate(gameSessionId);
                return new RegisterResponseGuess("=", count, "Угадал за " + count.toString() + ".");
            }
        } else {
            return new RegisterResponseGuess(null, null, "Session does not exist");
        }
    }

    public List<Account> users() {
        return new ArrayList<>(getMapAccount().values());
    }

    public String historyGames(String userName) {
        if (getMapHistoryGuess().containsKey(userName)) {
            Integer countGames = getMapHistoryGuess().get(userName).size();
            return countGames.toString() + " игры сыграно. Выберите нужную вам игру.";
        } else {
            return "Логин не найден.";
        }
    }

    public String resultGame(String userName, int numberGame) {
        if (getMapHistoryGuess().containsKey(userName)) {
            if (getMapHistoryGuess().get(userName).size() >= numberGame && numberGame > 0) {
                String result = "Сделано " + getMapHistoryGuess().get(userName).get(numberGame - 1).getAttempts().toString() + " попыток.";
                if (getMapHistoryGuess().get(userName).get(numberGame - 1).getWin()) {
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

    public String terminate(UUID id) {
        if (getMapSessionGuess().containsKey(id)) {
            getMapSessionGuess().remove(id);
            return "Session terminate";
        }
        return "Session does not exist";
    }
}
