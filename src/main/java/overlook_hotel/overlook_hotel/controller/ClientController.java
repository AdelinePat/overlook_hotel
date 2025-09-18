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

import java.nio.file.DirectoryStream.Filter;
import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;
    // private List<Client> clients;
    private FilterFields filterFields;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
        this.filterFields = new FilterFields();
    }

    // @RequestMapping(value = "/clients", method = {RequestMethod.GET, RequestMethod.POST})
    // public String clients(@RequestParam(required = false) String lastname,
    //                       @RequestParam(required = false) String firstname,
    //                       @RequestParam(required = false) String email,
    //                       @RequestParam(required = false) String phone,
    //                       @RequestParam(required = false) Long id,
    //                       Model model) {
    //     model.addAttribute("testBlabla", "les gens. coucou florence et thibault");

    //     lastname = lastname == null ? "" : lastname.trim();
    //     firstname = firstname == null ? "" : firstname.trim();
    //     email = email == null ? "" : email.toLowerCase().trim();
    //     phone = phone == null ? "" : phone.trim();

    //     FilterFields clientFilter = new FilterFields();
    //     FilterFields focus = new FilterFields();
    //     focus.setId(id);

    //     Client focusedClient = clientService.findById(id != null ? id : -1L);
    //     if (focusedClient != null) {
    //         focus.setId(focusedClient.getId().longValue());
    //         focus.setLastname(focusedClient.getLastname());
    //         focus.setFirstname(focusedClient.getFirstname());
    //         focus.setEmail(focusedClient.getEmail());
    //         focus.setPhone(focusedClient.getPhone());
    //     }

    //     model.addAttribute("filterField", clientFilter);
    //     model.addAttribute("focusedClient", focusedClient);
    //     List<Client> clients = clientService.findAllFiltered(lastname, firstname, email, phone);
    //     model.addAttribute("clients", clients);
        
    //     clientFilter.setLastname(lastname);
    //     clientFilter.setFirstname(firstname);
    //     clientFilter.setEmail(email);
    //     clientFilter.setPhone(phone);


        
        
    //     model.addAttribute("focusField", focus);
    //     model.addAttribute("title", "Clients");
    //     model.addAttribute("columns", List.of("ID", "Nom", "Prénom", "Email", "Téléphone"));
    //     model.addAttribute("rows", clients);
    //     model.addAttribute("entityType", "client");
    //     return "table";
    // }


        @RequestMapping(value = "/clients", method = {RequestMethod.GET, RequestMethod.POST})
    public String clients(
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Long id,
            Model model) {

        // ---- FILTRE ----
        lastname = lastname == null ? "" : lastname.trim();
        firstname = firstname == null ? "" : firstname.trim();
        email = email == null ? "" : email.toLowerCase().trim();
        phone = phone == null ? "" : phone.trim();
        id = id == null ? null : id;

        this.populateFilterFields(lastname, firstname, email, phone);

        List<Client> clients = clientService.findAllFiltered(
            this.filterFields.getLastname(),
            this.filterFields.getFirstname(),
            this.filterFields.getEmail(),
            this.filterFields.getPhone()
            );

        // FilterFields filterField = new FilterFields();
        FilterFields focusField = new FilterFields();
        focusField.setId(id);

        // ---- FOCUS ----
        Client focusedClient = null;
        if (id != null) {
            focusedClient = clientService.findById(id);
            focusField.setId(focusedClient.getId().longValue());
            focusField.setLastname(focusedClient.getLastname());    
            focusField.setFirstname(focusedClient.getFirstname());
            focusField.setEmail(focusedClient.getEmail());
            focusField.setPhone(focusedClient.getPhone());
        }

        // ---- MODEL ----
        model.addAttribute("clients", clients);
        model.addAttribute("focusedClient", focusedClient);
        model.addAttribute("focusField", focusField);
        model.addAttribute("filterField", this.filterFields);
        model.addAttribute("title", "Clients");
        model.addAttribute("columns", List.of("ID", "Nom", "Prénom", "Email", "Téléphone"));
        model.addAttribute("rows", clients);
        model.addAttribute("entityType", "client");

        return "table";
    }

    private void populateFilterFields(String lastname, String firstname, String email, String phone) {
        if (lastname != null && !lastname.isBlank()) {
            this.filterFields.setLastname(lastname);
        } 
        if (firstname != null && !firstname.isBlank()) {
            this.filterFields.setFirstname(firstname);
        }
        if (email != null && !email.isBlank()) {
            this.filterFields.setEmail(email);
        }
        if (phone != null && !phone.isBlank()) {
            this.filterFields.setPhone(phone);
        }

        if ((lastname == null || lastname.isBlank()) &&
            (firstname == null || firstname.isBlank()) &&
            (email == null || email.isBlank()) &&
            (phone == null || phone.isBlank())) {
                this.resetFilterFields();
        }
    }

    private void resetFilterFields() {
        this.filterFields = new FilterFields();
    }

}
