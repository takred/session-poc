package takred.setionpoc;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
public class GuessService {
    private Map<UUID, SessionGuess> mapSessionGuess = new HashMap<>();
    private Map<String, List<ResultGuess>> mapHistoryGuess = new HashMap<>();

    public Map<UUID, SessionGuess> getMapSessionGuess() {
        return mapSessionGuess;
    }

    public Map<String, List<ResultGuess>> getMapHistoryGuess() {
        return mapHistoryGuess;
    }
}
