package takred.setionpoc;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class LogoutController {

    LogoutService logoutService;
    public LogoutController(LogoutService logoutService){
        this.logoutService = logoutService;
    }

    @RequestMapping(value = "/logout/{loginName}")
    public LogoutResponse logout(@PathVariable("loginName") String loginName) {
        return logoutService.logout(loginName);
    }

}
