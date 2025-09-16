package overlook_hotel.overlook_hotel.controller;

import overlook_hotel.overlook_hotel.model.entity.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import overlook_hotel.overlook_hotel.service.ClientService;

import java.util.List;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
        public String index(Model model) {
        List<Client> clients = clientService.findAllFiltered("", "", "", "");
        model.addAttribute("clients", clients);
        model.addAttribute("testBlabla", "ELTIGANIIIIIII Salutation les gens. coucou florence et thibault");
        return "index";
    }
}
