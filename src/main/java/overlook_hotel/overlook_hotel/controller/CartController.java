package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import overlook_hotel.overlook_hotel.model.RoomReservationCart;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.RoomBonus;
import overlook_hotel.overlook_hotel.model.entity.Standing;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.repository.ClientRepository;
import overlook_hotel.overlook_hotel.service.RoomBonusService;
import overlook_hotel.overlook_hotel.service.RoomReservationService;
import overlook_hotel.overlook_hotel.service.RoomService;
import overlook_hotel.overlook_hotel.service.StandingService;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("roomReservationCart")
public class CartController {

    private final RoomService roomService;
    private final RoomBonusService roomBonusService;
    private final StandingService standingService;
    private final RoomReservationService roomReservationService;
    private final ClientRepository clientRepository;

    public CartController(RoomService roomService,
                          RoomBonusService roomBonusService,
                          StandingService standingService,
                          RoomReservationService roomReservationService,
                          ClientRepository clientRepository) {

        this.roomService = roomService;
        this.roomBonusService = roomBonusService;
        this.standingService = standingService;
        this.roomReservationService = roomReservationService;
        this.clientRepository = clientRepository;
    }

    @ModelAttribute("roomReservationCart")
    public RoomReservationCart createCart() {
        RoomReservationCart cart = new RoomReservationCart();
        cart.setRooms(new ArrayList<>());
        return cart;
    }


    @GetMapping("/cart")
    public String showCart(@ModelAttribute("roomReservationCart") RoomReservationCart cart,
                           Model model) {

        List<Long> ids = cart.getRooms().stream()
                .map(RoomReservationFields::getIdRoom)
                .toList();

        Map<Long, Room> roomsById = roomService.findAllByIds(ids)
                .stream()
                .collect(Collectors.toMap(Room::getId, Function.identity()));

        cart.getRooms().forEach(roomReservation -> {

            Room room = roomsById.get(roomReservation.getIdRoom());
            BigDecimal basePrice = room.getNightPrice();

            // Base room bonuses
            BigDecimal baseBonusTotal = room.getBonuses().stream()
                    .map(RoomBonus::getDailyPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Additional bonuses
            BigDecimal additionalBonusTotal = BigDecimal.ZERO;
            if (roomReservation.getAdditionalBonuses() != null) {
                for (RoomBonusEnum bonusEnum : roomReservation.getAdditionalBonuses()) {
                    RoomBonus bonusEntity = roomBonusService.findBonusByType(bonusEnum);
                    if (bonusEntity != null) additionalBonusTotal = additionalBonusTotal.add(bonusEntity.getDailyPrice());
                }
            }

            long nights = ChronoUnit.DAYS.between(roomReservation.getStartDate(), roomReservation.getEndDate());
            if (nights < 1) nights = 1;

            BigDecimal nightlyTotal = basePrice.add(baseBonusTotal).add(additionalBonusTotal);
            BigDecimal totalPrice = nightlyTotal.multiply(BigDecimal.valueOf(nights));

            roomReservation.setTotalPriceWithAdditional(totalPrice);
        });
        BigDecimal totalCartPrice = cart.getRooms().stream()
                .map(RoomReservationFields::getTotalPriceWithAdditional)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalCartPrice", totalCartPrice);

        model.addAttribute("cartItems", cart.getRooms());
        return "cart";
    }

    @PostMapping("/cart")
    public String addOrDeleteFromCart(
            @RequestParam(value = "deleteIndex", required = false) Integer deleteIndex,
            @RequestParam(value = "resetCart", required = false) Boolean resetCart,
            @RequestParam(value = "confirmCart", required = false) Boolean confirmCart,
            @ModelAttribute RoomReservationFields roomFields, // only needed for adding items
            @ModelAttribute("roomReservationCart") RoomReservationCart cart,
            SessionStatus sessionStatus,
            RedirectAttributes redirectAttributes
    ) {

        if (resetCart != null && resetCart) {
            cart.getRooms().clear();
            redirectAttributes.addFlashAttribute("message", "Votre panier a été vidé avec succès.");
        } else if (deleteIndex != null) {
            if (deleteIndex >= 0 && deleteIndex < cart.getRooms().size()) {
                int index = deleteIndex;
//                                                  cart.getRooms().remove(deleteIndex);
                RoomReservationFields removedRoom = cart.getRooms().remove(index);
                redirectAttributes.addFlashAttribute("message",
                        "La chambre n°" + removedRoom.getRoomNumber() + " a été retirée du panier.");
            }
        } else if (roomFields.getIdRoom() != null) { // add new item only if adding
            if (roomFields.getStandingString() != null) {
                roomFields.setStanding(standingService.findStandingByName(roomFields.getStandingString()));
            }
            RoomReservationFields newFields = this.initFields(roomFields);
            cart.getRooms().add(newFields);
            redirectAttributes.addFlashAttribute("message", "La chambre a été ajoutée au panier.");
        }

        if (Boolean.TRUE.equals(confirmCart) && !cart.getRooms().isEmpty()) {
            Optional<Client> dummyClient = clientRepository.findById(1L);
            if (dummyClient.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de confirmer la réservation : client introuvable.");
                return "redirect:/cart";
            }

            try {
                roomReservationService.saveCartAsReservation(cart, dummyClient.get().getId());
                cart.getRooms().clear();
                sessionStatus.setComplete();
                redirectAttributes.addFlashAttribute("message", "Votre réservation a été confirmée avec succès !");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la sauvegarde de la réservation.");
            }
        }

        return "redirect:/cart";
    }

    private RoomReservationFields initFields(RoomReservationFields roomFields) {
        System.out.println("\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\tDANS INITFIELDS!! " + roomFields.getStartDate() + " " + roomFields.getEndDate());
        RoomReservationFields newFields = new RoomReservationFields();
        newFields.setIdRoom(roomFields.getIdRoom());
        newFields.setStartDate(roomFields.getStartDate());
        newFields.setEndDate(roomFields.getEndDate());
        newFields.setStanding(roomFields.getStanding());
        newFields.setAdditionalBonuses(new ArrayList<>(roomFields.getAdditionalBonuses() != null ? roomFields.getAdditionalBonuses() : List.of()));
        return newFields;
    }
}
