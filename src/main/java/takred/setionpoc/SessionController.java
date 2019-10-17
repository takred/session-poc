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

    @RequestMapping(value = "/start")
    public String start() {
        UUID id = UUID.randomUUID();
        mapSession.put(id, new Session(id, 0, ThreadLocalRandom.current().nextInt(0, 10000)));
        return id.toString();
    }

    @RequestMapping(value = "/count/{id}")
    public String count(@PathVariable("id") UUID id) {
        if (mapSession.containsKey(id)) {
//            mapSession.put(id, mapSession.get(id).setCountLogin(mapSession.get(id).getCountLogin() + 1));
            return mapSession.get(id).getCountLogin().toString();
        }
        return "Session does not exist";
    }

    @RequestMapping(value = "/guess/{id}/{number}")
    public String guess(@PathVariable("id") UUID id, @PathVariable("number") Integer number) {
        if (mapSession.containsKey(id)) {
            mapSession.put(id, mapSession.get(id).setCountLogin(mapSession.get(id).getCountLogin() + 1));
            if (mapSession.get(id).getRandomNumber() > number) {
                return "Число больше.";
            } else if (mapSession.get(id).getRandomNumber() < number) {
                return "Число меньше.";
            } else {
                Integer count = mapSession.get(id).getCountLogin();
                terminate(id);
                return "Угадал за " + count.toString() + ".";
            }
        }else {
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
}
