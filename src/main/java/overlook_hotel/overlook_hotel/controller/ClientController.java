package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.FilterFields;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.model.enumList.Job;
import overlook_hotel.overlook_hotel.service.ClientService;
import overlook_hotel.overlook_hotel.service.EmployeeService;

import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

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

        FilterFields clientFilter = new FilterFields();
        model.addAttribute("filterField", clientFilter);
        List<Client> clients = clientService.findAllFiltered(lastname, firstname, email, phone);
        model.addAttribute("clients", clients);

        clientFilter.setLastname(lastname);
        clientFilter.setFirstname(firstname);
        clientFilter.setEmail(email);
        clientFilter.setPhone(phone);


        model.addAttribute("title", "Clients");
        model.addAttribute("columns", List.of("ID", "Nom", "Prénom", "Email", "Téléphone"));
        model.addAttribute("rows", clients);
        model.addAttribute("entityType", "client");
        return "table";
    }

}
