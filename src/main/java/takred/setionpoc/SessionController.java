package takred.setionpoc;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/")
public class SessionController {
    private List<Session> allSession = new ArrayList<>();
    private Map<UUID, Session> mapSession = new HashMap<>();

    @RequestMapping(value = "/start")
    public String start() {
        UUID id = UUID.randomUUID();
        mapSession.put(id, new Session(id, 0));
        return id.toString();
    }

    @RequestMapping(value = "/visit/{id}")
    public String visit(@PathVariable("id") UUID id) {
        if (mapSession.containsKey(id)) {
            mapSession.put(id, mapSession.get(id).setCountLogin(mapSession.get(id).getCountLogin() + 1));
            return mapSession.get(id).getCountLogin().toString();
        }
        return "Session does not exist";
    }

    @RequestMapping(value = "/terminate/{id}")
    public String terminate(@PathVariable("id") UUID id) {
        if (mapSession.containsKey(id)){
            mapSession.remove(id);
            return "Session terminate";
        }
        return "Session does not exist";
    }
}
