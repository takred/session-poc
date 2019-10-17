package takred.setionpoc;

import java.util.UUID;

public class Session {
    private final UUID id;
    private final Integer countLogin;
    private final Integer randomNumber;

    public Session(UUID id, Integer countLogin, Integer randomNumber) {
        this.id = id;
        this.countLogin = countLogin;
        this.randomNumber = randomNumber;
    }

    public UUID getId() {
        return id;
    }

    public Session setId(UUID id) {
        return new Session(id, countLogin, randomNumber);
    }

    public Integer getCountLogin() {
        return countLogin;
    }

    public Session setCountLogin(Integer countLogin) {
        return new Session(id, countLogin, randomNumber);
    }

    public Integer getRandomNumber() {
        return randomNumber;
    }
}
