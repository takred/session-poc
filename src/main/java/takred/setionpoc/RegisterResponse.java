package takred.setionpoc;

import java.util.UUID;

public class RegisterResponse {
    private final UUID uuid;
    private final String message;

    public RegisterResponse(UUID uuid, String message) {
        this.uuid = uuid;
        this.message = message;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getMessage() {
        return message;
    }
}
