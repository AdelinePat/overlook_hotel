package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.ClientFilter;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.service.ClientService;

import java.util.List;

@Controller
public class TableController {
    private final ClientService clientService;

    public TableController(ClientService clientService) {
        this.clientService = clientService;
    }

//    @GetMapping("/clients")
//    public String index(Model model) {
//        List<Client> clients = clientService.findAllFiltered("", "", "", "");
//        model.addAttribute("clients", clients);
//        model.addAttribute("clientFilter", new ClientFilter()); // ✅ add this line
//        model.addAttribute("testBlabla", "les gens. coucou florence et thibault");
//        return "table";
//    }

    @RequestMapping(value = "/clients", method = {RequestMethod.GET, RequestMethod.POST})
    public String clients(@RequestParam(required = false) String lastname,
                          @RequestParam(required = false) String firstname,
                          @RequestParam(required = false) String email,
                          @RequestParam(required = false) String phone,
                          Model model) {
        model.addAttribute("testBlabla", "les gens. coucou florence et thibault");

        lastname = lastname == null ? "" : lastname.trim();
        firstname = firstname == null ? "" : firstname.trim();
        email = email == null ? "" : email.toLowerCase().trim();
        phone = phone == null ? "" : phone.trim();

        List<Client> clients = clientService.findAllFiltered(lastname, firstname, email, phone);

        ClientFilter clientFilter = new ClientFilter();
        clientFilter.setLastname(lastname);
        clientFilter.setFirstname(firstname);
        clientFilter.setEmail(email);
        clientFilter.setPhone(phone);

        model.addAttribute("clients", clients);
        model.addAttribute("clientFilter", clientFilter);
        return "table";
    }


//    @PostMapping("/clients")
//    public String filterClients(@RequestParam(required = false) String lastname,
//                                @RequestParam(required = false) String firstname,
//                                @RequestParam(required = false) String email,
//                                @RequestParam(required = false) String phone,
//                                Model model) {
//        lastname = lastname == null ? "" : lastname.trim();
//        firstname = firstname == null ? "" : firstname.trim();
//        email = email == null ? "" : email.toLowerCase().trim();
//        phone = phone == null ? "" : phone.trim();
//
//        List<Client> filteredClients = clientService.findAllFiltered(lastname, firstname, email, phone);
//        model.addAttribute("clientFilter", new ClientFilter()); // optionally prefill
//        model.addAttribute("clients", filteredClients);
//        return "table";
//    }
}
