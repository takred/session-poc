package takred.setionpoc.guess;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import takred.setionpoc.*;

import java.util.*;

@RestController
@RequestMapping(value = "/guess")
public class GuessController {
    private final GuessService guessService;

    public GuessController(GuessService guessService) {
        this.guessService = guessService;
    }
    private Map<UUID, SessionGuess> getMapSessionGuess(){
        return guessService.getMapSessionGuess();
    }

    private Map<String, List<ResultGuess>> getMapHistoryGuess(){
        return guessService.getMapHistoryGuess();
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

    @RequestMapping(value = "/end_game/{id}")
    public String terminate(@PathVariable("id") UUID id) {
        Account account = guessService.getAccount(id);
        if (account != null) {
            if (getMapSessionGuess().containsKey(account.getSessionIdGuess())) {
                return guessService.terminate(account.getLoginSessionId());
            }
        }
        return "Session does not exist";
    }
}
