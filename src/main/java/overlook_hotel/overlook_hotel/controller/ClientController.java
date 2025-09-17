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
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
        public String index(Model model) {
//        List<Client> clients = clientService.findAllFiltered("", "", "", "");
//        model.addAttribute("clients", clients);
//        model.addAttribute("testBlabla", "FONCTIOOOOONNE !!!! les gens. coucou florence et thibault");
        List<Client> clients = clientService.findAllFiltered("", "", "", "");
        model.addAttribute("clients", clients);
        model.addAttribute("clientFilter", new ClientFilter()); // ✅ add this line
        model.addAttribute("testBlabla", "fdp !!!! les gens. coucou florence et thibault");
        return "index";
    }

//    @PostMapping("/filterForm")
//    public String submitForm(@ModelAttribute Client client, Model model) {
//        model.addAttribute("submitFilter", client);
//        return "redirect:/";
//    }


//    @PostMapping("/filterForm")
//    public String filterClients(@ModelAttribute("clientFilter") ClientFilter filter, Model model) {
//        List<Client> filteredClients = clientService.findAllFiltered(
//                filter.getLastname(),
//                filter.getFirstname(),
//                filter.getEmail(),
//                filter.getPhone()
//        );
//
//        model.addAttribute("clientFilter", filter);
//        model.addAttribute("clients", filteredClients);
//        return "redirect:/"; // reload same page with filtered results
//    }

    @GetMapping("/filterForm")
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
        return "index";
    }


}
