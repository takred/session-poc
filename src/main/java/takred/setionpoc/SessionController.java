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
//        allSession.add(new Session( UUID.randomUUID(), 0));
//        return allSession.get(allSession.size() - 1).getId().toString();
        return id.toString();
    }

    @RequestMapping(value = "/visit/{id}")
    public String visit(@PathVariable("id") UUID id) {
        if (mapSession.containsKey(id)) {
            mapSession.put(id, mapSession.get(id).setCountLogin(mapSession.get(id).getCountLogin() + 1));
            return mapSession.get(id).getCountLogin().toString();
        }
//        for (int i = 0; i < allSession.size(); i++) {
//            if (allSession.get(i).getId().equals(id)) {
//                allSession.set(i, allSession.get(i).setCountLogin(allSession.get(i).getCountLogin() + 1));
//                return allSession.get(i).getCountLogin().toString();
//            }
//        }
        return "Session does not exist";
    }

    @RequestMapping(value = "/terminate/{id}")
    public String terminate(@PathVariable("id") UUID id) {
        if (mapSession.containsKey(id)){
            mapSession.remove(id);
            return "Session terminate";
        }
//        for (int i = 0; i < allSession.size(); i++) {
//            if (allSession.get(i).getId().equals(id)) {
//                deleteElement(i);
//                return "Session terminate";
//            }
//        }
        return "Session does not exist";
    }

    public void deleteElement(int index) {
        List<Session> copyList = new ArrayList<>(allSession);
        allSession = new ArrayList<>();
        for (int i = 0; i < copyList.size(); i++) {
            if (i != index) {
                allSession.add(new Session(copyList.get(i).getId(), copyList.get(i).getCountLogin()));
            }
        }
    }
}
