package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.FilterFields;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.service.ClientService;
import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;
    private FilterFields filterFields;
    private FilterFields focusedField;
    private Client focusedClient;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
        this.filterFields = new FilterFields();
        this.focusedField = new FilterFields();
        this.focusedClient = new Client();
    }

        @RequestMapping(value = "/clients", method = {RequestMethod.GET, RequestMethod.POST})
    public String clients(
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Boolean reset,
            Model model) {

        // Handle reset button
        if (reset != null && reset) {
            this.resetFocusedField();
            this.filterFields = new FilterFields();
            List<Client> clients = clientService.findAllFiltered("", "", "", "");
            model.addAttribute("clients", clients);
            model.addAttribute("focusedClient", null);
            model.addAttribute("focusField", new FilterFields());
            model.addAttribute("filterField", new FilterFields());
            model.addAttribute("title", "Clients");
            model.addAttribute("titlePage", "Gestion des clients");
            model.addAttribute("columns", List.of("Nom", "Prénom", "Email", "Téléphone"));
            model.addAttribute("rows", clients);
            model.addAttribute("entityType", "client");
            this.focusedClient = null;
            return "table";
        }

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

        // ---- FOCUS ----
        this.populateFocusField(id);
        if (this.focusedField.getId() != null) {
            this.focusedClient = clientService.findById(this.focusedField.getId());
        } else {
            this.resetFocusedField();
        }

        // ---- MODEL ----
        model.addAttribute("clients", clients);
        model.addAttribute("focusedClient", focusedClient);
        model.addAttribute("focusField", this.focusedField);
        model.addAttribute("filterField", this.filterFields);
        model.addAttribute("title", "Clients");
        model.addAttribute("titlePage", "Gestion des clients");
        model.addAttribute("columns", List.of("Nom", "Prénom", "Email", "Téléphone"));
        model.addAttribute("rows", clients);
        model.addAttribute("entityType", "client");

        return "table";
    }

    private void populateFilterFields(String lastname, String firstname, String email, String phone) {
        if (lastname != null) this.filterFields.setLastname(lastname.trim());
        if (firstname != null) this.filterFields.setFirstname(firstname.trim());
        if (email != null) this.filterFields.setEmail(email.toLowerCase().trim());
        if (phone != null) this.filterFields.setPhone(phone.trim());
    }

    private void resetFocusedField() {
        this.focusedField.setId(null);
        this.focusedField.setLastname(null);
        this.focusedField.setFirstname(null);
        this.focusedField.setEmail(null);
        this.focusedField.setPhone(null);
        this.focusedField.setPassword(null);
        this.focusedField.setJob(null);
        this.focusedField.setPriceRange(null);
    }

    private void populateFocusField(Long id) {
        if (id != null) {
            this.focusedField.setId(id);
            this.focusedClient = clientService.findById(id);

            if (this.focusedClient != null) {
                this.focusedField.setId(this.focusedClient.getId().longValue());
                this.focusedField.setLastname(this.focusedClient.getLastname());    
                this.focusedField.setFirstname(this.focusedClient.getFirstname());
                this.focusedField.setEmail(this.focusedClient.getEmail());
                this.focusedField.setPhone(this.focusedClient.getPhone());
                
            } else {
                this.resetFocusedField();
            }
        } else {
            this.resetFocusedField();
        }
    }

}
