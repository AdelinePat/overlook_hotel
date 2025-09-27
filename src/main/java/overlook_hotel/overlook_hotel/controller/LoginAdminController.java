package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginAdminController {

    @GetMapping("/login-admin")
    public String showAdminLoginForm() {
        return "login-admin";
    }
}
