package overlook_hotel.overlook_hotel.controller;

import overlook_hotel.overlook_hotel.entity.Client;
import overlook_hotel.overlook_hotel.repository.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("testBlabla", "HAAAAAAAA salutation les gens. coucou florence et thibault");
        return "index";
    }
}
