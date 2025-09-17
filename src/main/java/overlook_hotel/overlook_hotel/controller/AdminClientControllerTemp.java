package overlook_hotel.overlook_hotel.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import overlook_hotel.overlook_hotel.model.ClientFilter;
import overlook_hotel.overlook_hotel.model.entity.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import overlook_hotel.overlook_hotel.service.ClientService;

import java.util.List;

import static org.hibernate.validator.internal.util.ReflectionHelper.typeOf;

@Controller
public class AdminClientControllerTemp {

    private final ClientService clientService;

    public AdminClientControllerTemp(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/admin-client-temp")
    public String adminClient(Model model) {
        List<Client> clients = clientService.findAllFiltered("", "", "", "");
        model.addAttribute("clients", clients);
        model.addAttribute("clientFilter", new ClientFilter());
        model.addAttribute("testAdminClient", "test page d'administration des clients. Temporaire");
        return "admin-client-temp";
    }

    @GetMapping("/admin-client-filterform-temp")
    public String filterClients(@RequestParam(required = false) String lastname,
                                @RequestParam(required = false) String firstname,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String phone,
                                Model model) {
        lastname = lastname == null ? "" : lastname.trim();
        firstname = firstname == null ? "" : firstname.trim();
        email = email == null ? "" : email.toLowerCase().trim();
        phone = phone == null ? "" : phone.trim();

        List<Client> filteredClients = clientService.findAllFiltered(lastname, firstname, email, phone);
        model.addAttribute("clientFilter", new ClientFilter()); // optionally prefill
        model.addAttribute("clients", filteredClients);
        return "admin-client-temp";
    }


}
