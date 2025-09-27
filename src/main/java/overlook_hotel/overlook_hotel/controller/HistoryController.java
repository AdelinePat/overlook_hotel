package overlook_hotel.overlook_hotel.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;
import overlook_hotel.overlook_hotel.service.ClientService;
import overlook_hotel.overlook_hotel.service.RoomReservationService;

import java.util.List;

@Controller
public class HistoryController {

    private final RoomReservationService roomReservationService;
    private final ClientService clientService;

    public HistoryController(RoomReservationService roomReservationService,
                             ClientService clientService) {
        this.roomReservationService = roomReservationService;
        this.clientService = clientService;
    }

    @GetMapping("/history")
    public String showHistory(Model model, Authentication authentication) {
        String username = authentication.getName(); // Spring Security username
        Client client = clientService.findByEmail(username); // assuming username = email

        if (client == null) {
            return "redirect:/login"; // or show error
        }

        List<RoomReservation> reservations = roomReservationService.getAllReservationFromClient(client.getId());

        model.addAttribute("title", "Historique des réservations");
        model.addAttribute("reservations", reservations);

        return "history"; // maps to history.html
    }
}
