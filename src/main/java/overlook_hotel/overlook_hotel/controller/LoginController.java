
package overlook_hotel.overlook_hotel.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
    @GetMapping("/")
    public String root() {
        return "login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
