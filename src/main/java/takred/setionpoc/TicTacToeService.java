package takred.setionpoc;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TicTacToeService {
    private Map<UUID, SessionTicTacToe> mapSessionTicTacToe = new HashMap<>();
    private Map<String, List<ResultTicTacToe>> mapHistoryTicTacToe = new HashMap<>();

    public Map<UUID, SessionTicTacToe> getMapSessionTicTacToe() {
        return mapSessionTicTacToe;
    }

    public Map<String, List<ResultTicTacToe>> getMapHistoryTicTacToe() {
        return mapHistoryTicTacToe;
    }
}
