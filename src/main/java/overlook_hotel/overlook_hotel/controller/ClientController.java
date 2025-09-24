package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.FilterFields;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.service.ClientService;
import overlook_hotel.overlook_hotel.util.InputSanitizer;

import java.util.List;

@Controller
public class ClientController extends AbstractEntityController<Client, FilterFields> {
    private final ClientService clientService;
    private FilterFields filterFields;

    public ClientController(ClientService clientService) {
    this.clientService = clientService;
    this.filterFields = new FilterFields();
    this.focusedField = new FilterFields();
    this.focusedEntity = null;
    }

    @RequestMapping(value = "/clients", method = {RequestMethod.GET, RequestMethod.POST})
    public String clients(
        @RequestParam(required = false) String lastname,
        @RequestParam(required = false) String firstname,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) Boolean reset,
        @RequestParam(required = false) String action,
        Model model) {

        // Handle reset button
        if (reset != null && reset) {
            this.resetFocusedField(f -> new FilterFields());
            this.filterFields = new FilterFields();
            List<Client> clients = clientService.findAllFiltered("", "", "", "");
            this.populateModel(model, clients, "client", List.of("Nom", "Prénom", "Email", "Téléphone"), null);
            return "table";
        }

        // Handle add, update, delete actions
        if (action != null) {
            if (action.equals("search")) {
                this.populateFilterFields(
                    InputSanitizer.sanitize(lastname),
                    InputSanitizer.sanitize(firstname),
                    InputSanitizer.sanitize(email),
                    InputSanitizer.sanitize(phone)
                );
            }
            else if (action.equals("add") && id == null) {
                try {
                    Client newClient = new Client();
                    newClient.setLastname(InputSanitizer.sanitize(lastname));
                    newClient.setFirstname(InputSanitizer.sanitize(firstname));
                    newClient.setEmail(InputSanitizer.sanitize(email));
                    newClient.setPhone(InputSanitizer.sanitize(phone));
                    newClient.setSalt("defaultSalt");
                    newClient.setPassword("defaultPassword");
                    clientService.save(newClient);
                    model.addAttribute("message", "Ajout réussi !");
                } catch (Exception e) {
                    model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
                }
                this.resetFocusedField(f -> new FilterFields());
                this.filterFields = new FilterFields();
            } else if (action.equals("update") && id != null) {
                try {
                    Client clientToUpdate = clientService.findById(id);
                    if (clientToUpdate != null) {
                        clientToUpdate.setLastname(InputSanitizer.sanitize(lastname));
                        clientToUpdate.setFirstname(InputSanitizer.sanitize(firstname));
                        clientToUpdate.setEmail(InputSanitizer.sanitize(email));
                        clientToUpdate.setPhone(InputSanitizer.sanitize(phone));
                        clientService.save(clientToUpdate);
                        model.addAttribute("message", "Modification réussie !");
                    }
                } catch (Exception e) {
                    model.addAttribute("error", "Erreur lors de la modification : " + e.getMessage());
                }
                this.resetFocusedField(f -> new FilterFields());
                this.filterFields = new FilterFields();
            } else if (action.equals("delete") && id != null) {
                try {
                    clientService.deleteById(id);
                    model.addAttribute("message", "Suppression réussie !");
                } catch (Exception e) {
                    model.addAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
                }
                this.resetFocusedField(f -> new FilterFields());
                this.filterFields = new FilterFields();
            }
        }

        lastname = lastname == null ? "" : lastname.trim();
        firstname = firstname == null ? "" : firstname.trim();
        email = email == null ? "" : email.toLowerCase().trim();
        phone = phone == null ? "" : phone.trim();
        id = id == null ? null : id;

        List<Client> clients = clientService.findAllFiltered(
            this.filterFields.getLastname(),
            this.filterFields.getFirstname(),
            this.filterFields.getEmail(),
            this.filterFields.getPhone()
        );

        this.populateFocusField(id,
            clientService::findById,
            client -> {
                FilterFields focusedField = new FilterFields();
                focusedField.setId(client.getId().longValue());
                focusedField.setLastname(client.getLastname());
                focusedField.setFirstname(client.getFirstname());
                focusedField.setEmail(client.getEmail());
                focusedField.setPhone(client.getPhone());
                return focusedField;
            },
            filter -> new FilterFields()
        );

        this.populateModel(model, clients, "client", List.of("Nom", "Prénom", "Email", "Téléphone"), null);

        return "table";
    }

    private void populateFilterFields(String lastname, String firstname, String email, String phone) {
        if (lastname != null) this.filterFields.setLastname(lastname.trim());
        if (firstname != null) this.filterFields.setFirstname(firstname.trim());
        if (email != null) this.filterFields.setEmail(email.toLowerCase().trim());
        if (phone != null) this.filterFields.setPhone(phone.trim());
    }

}
