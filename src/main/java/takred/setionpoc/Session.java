package takred.setionpoc;

import java.util.UUID;

public class Session {
    private final UUID id;
    private final Integer countLogin;

    public Session(UUID id, Integer countLogin) {
        this.id = id;
        this.countLogin = countLogin;
    }

    public UUID getId() {
        return id;
    }

    public Session setId(UUID id) {
        return new Session(id, countLogin);
    }

    public Integer getCountLogin() {
        return countLogin;
    }

    public Session setCountLogin(Integer countLogin) {
        return new Session(id, countLogin);
    }
}
