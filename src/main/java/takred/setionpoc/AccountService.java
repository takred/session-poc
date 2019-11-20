package takred.setionpoc;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

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