package takred.setionpoc;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class AccountService {
    private Map<String, Account> mapAccount = new HashMap<>();

    public Map<String, Account> getMapAccount() {
        return mapAccount;
    }
     public Account getAccount(String login) {
        return mapAccount.get(login);
     }
}
