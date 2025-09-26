package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class LoginAdminController {

    // @Autowired
    // private AuthenticationManager authenticationManager;

    @GetMapping("/login-admin")
    public String showAdminLoginForm() {
        return "login-admin";
    }

    // @PostMapping("/login-admin")
    // public String processAdminLogin(@RequestParam String username,
    //                                 @RequestParam String password,
    //                                 Model model) {
    //     try {
    //         Authentication auth = authenticationManager.authenticate(
    //             new UsernamePasswordAuthenticationToken(username, password)
    //         );
    //         if (auth.isAuthenticated() && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
    //             return "redirect:/clients";
    //         } else {
    //             model.addAttribute("error", "Accès réservé aux administrateurs.");
    //             return "login-admin";
    //         }
    //     } catch (AuthenticationException e) {
    //         model.addAttribute("error", "Email ou mot de passe incorrect.");
    //         return "login-admin";
    //     }
    // }
}
