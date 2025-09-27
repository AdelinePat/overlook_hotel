package overlook_hotel.overlook_hotel.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import overlook_hotel.overlook_hotel.model.ReservationHistoryDTO;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;
import overlook_hotel.overlook_hotel.service.ClientService;
import overlook_hotel.overlook_hotel.service.RoomReservationService;

import java.time.temporal.ChronoUnit;
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
        model.addAttribute("focusedReservation", null);

        List<ReservationHistoryDTO> reservations = roomReservationService.getAllReservationHistoryFromClient(client.getId());

        model.addAttribute("title", "Historique des réservations");
        model.addAttribute("reservations", reservations);
        model.addAttribute("columns", List.of("Création", "Début", "Fin", "Nombre de chambres", "Nombre de nuits", "Total"));

        return "history"; // maps to history.html
    }

    @PostMapping("/history")
    public String showReservationFocus(@RequestParam Long id, Model model, Authentication authentication) {
        String username = authentication.getName();
        Client client = clientService.findByEmail(username);
        if (client == null) return "redirect:/login";

        RoomReservation reservation = roomReservationService.getAllReservationFromClient(client.getId())
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (reservation == null) return "redirect:/history";

        ReservationHistoryDTO focusedDto = new ReservationHistoryDTO(
                reservation.getId(),
                reservation.getCreationDate(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                (int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()),
                reservation.getRoomLinks().size(),
                reservation.getPaymentDate(),
                reservation.getTotalPrice(),
                null
        );

        roomReservationService.setReservationWithRooms(focusedDto, reservation);


        model.addAttribute("focusedReservation", focusedDto);

        // also keep the main table
        List<ReservationHistoryDTO> reservations = roomReservationService.getAllReservationHistoryFromClient(client.getId());
        model.addAttribute("reservations", reservations);
        model.addAttribute("columns", List.of("Création", "Début", "Fin", "Nombre de chambres", "Nombre de nuits", "Total"));
        model.addAttribute("title", "Historique des réservations");

        return "history";
    }
}
