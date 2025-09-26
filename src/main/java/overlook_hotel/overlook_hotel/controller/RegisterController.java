package overlook_hotel.overlook_hotel.controller;

import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.service.ClientService;
import overlook_hotel.overlook_hotel.util.InputValidator;
import overlook_hotel.overlook_hotel.util.RandomizeSalt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    @Autowired
    private ClientService clientService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerClient(@RequestParam String lastname,
                                 @RequestParam String firstname,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        // Validate input
        if (!InputValidator.isValidWord(lastname)) {
            model.addAttribute("error", "Nom invalide.");
            return "register";
        }
        if (!InputValidator.isValidWord(firstname)) {
            model.addAttribute("error", "Prénom invalide.");
            return "register";
        }
        if (!InputValidator.isValidEmail(email)) {
            model.addAttribute("error", "Email invalide.");
            return "register";
        }
        if (!InputValidator.isValidPhone(phone)) {
            model.addAttribute("error", "Téléphone invalide.");
            return "register";
        }
        if (!InputValidator.isValidPassword(password)) {
            model.addAttribute("error", "Mot de passe invalide (min 6 caractères).");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            return "register";
        }
        if (clientService.findByEmail(email) != null) {
            model.addAttribute("error", "Un compte existe déjà avec cet email.");
            return "register";
        }
        // Encrypt password
        String hashedPassword = passwordEncoder.encode(password);
        String salt = RandomizeSalt.generateSalt(16); // 16 bytes (128 bits)
        Client client = new Client();
        client.setLastname(lastname);
        client.setFirstname(firstname);
        client.setEmail(email);
        client.setPhone(phone);
        client.setPassword(hashedPassword);
        client.setSalt(salt);
    clientService.save(client);
    return "redirect:/login?registered";
    }
}
