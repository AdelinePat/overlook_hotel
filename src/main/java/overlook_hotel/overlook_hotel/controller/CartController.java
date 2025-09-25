package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import overlook_hotel.overlook_hotel.model.RoomReservationCart;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.RoomBonus;
import overlook_hotel.overlook_hotel.model.entity.Standing;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.service.RoomBonusService;
import overlook_hotel.overlook_hotel.service.RoomService;
import overlook_hotel.overlook_hotel.service.StandingService;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("roomReservationCart")
public class CartController {

    private final RoomService roomService;
    private final RoomBonusService roomBonusService;
    private final StandingService standingService;

    public CartController(RoomService roomService,
                          RoomBonusService roomBonusService,
                          StandingService standingService) {
        this.roomService = roomService;
        this.roomBonusService = roomBonusService;
        this.standingService = standingService;
    }

    // Initialize session cart once
    @ModelAttribute("roomReservationCart")
    public RoomReservationCart createCart() {
        RoomReservationCart cart = new RoomReservationCart();
        cart.setRooms(new ArrayList<>());
        return cart;
    }

    // POST /cart → Add room to cart
//    @PostMapping("/cart")
//    public String addToCart(@ModelAttribute RoomReservationFields roomFields,
//                            @ModelAttribute("roomReservationCart") RoomReservationCart cart) {
//
//        // Resolve Standing entity
//        if (roomFields.getStandingString() != null) {
//            Standing standing = standingService.findStandingByName(roomFields.getStandingString());
//            roomFields.setStanding(standing);
//        }
//
//        // Convert additional bonuses to enum
//        if (roomFields.getAdditionalBonuses() != null) {
//            List<RoomBonusEnum> enumBonuses = new ArrayList<>();
//            System.out.println("\n\n\n\n\n\n\n\n\n" + roomFields.getAdditionalBonuses());
//            for (RoomBonusEnum bonus : roomFields.getAdditionalBonuses()) {
//                try {
//                    System.out.println("\n\n\n\n\n\n\n\n\n" + bonus);
//                    enumBonuses.add(RoomBonusEnum.valueOf(bonus.name()));
//                } catch (IllegalArgumentException ignored) {}
//            }
//            roomFields.setAdditionalBonuses(enumBonuses);
//        }
//
//        cart.getRooms().add(roomFields);
//        return "redirect:/cart"; // Show updated cart
//    }

    // GET /cart → Show cart contents
    @GetMapping("/cart")
    public String showCart(@ModelAttribute("roomReservationCart") RoomReservationCart cart,
                           Model model) {

        // Calculate total price for each room
        cart.getRooms().forEach(roomReservation -> {
            Room room = roomService.findById(roomReservation.getIdRoom());
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

        model.addAttribute("cartItems", cart.getRooms());
        model.addAttribute("totalCartPrice", totalCartPrice);

        model.addAttribute("cartItems", cart.getRooms());
        return "cart";
    }

    // POST /checkout → Save reservations and clear session cart
    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("roomReservationCart") RoomReservationCart cart,
                           SessionStatus sessionStatus) {

        for (RoomReservationFields fields : cart.getRooms()) {
            Room room = roomService.findById(fields.getIdRoom());

            // Example: save reservation (your service logic)
            // RoomReservation reservation = new RoomReservation();
            // reservation.setStartDate(fields.getStartDate());
            // reservation.setEndDate(fields.getEndDate());
            // reservation.setStatus(false);
            // roomReservationService.save(reservation, room, fields.getAdditionalBonuses());
        }

        cart.getRooms().clear();
        sessionStatus.setComplete(); // Remove session attribute
        return "redirect:/confirmation";
    }


    @PostMapping("/cart")
    public String addOrDeleteFromCart(@RequestParam(value = "deleteIndex", required = false) Integer deleteIndex,
                                      @RequestParam(value = "resetCart", required = false) Boolean resetCart,
                                      @ModelAttribute RoomReservationFields roomFields,
                                      @ModelAttribute("roomReservationCart") RoomReservationCart cart,
                                      @ModelAttribute("roomReservationFilter") RoomReservationFields filterFields) {


        filterFields.setStartDate(roomFields.getStartDate());
        filterFields.setEndDate(roomFields.getEndDate());


        if (resetCart != null && resetCart) {
            // 🗑 Clear the entire cart
            cart.getRooms().clear();
        } else if (deleteIndex != null) {
            // 🗑 Remove single item by index
            if (deleteIndex >= 0 && deleteIndex < cart.getRooms().size()) {
                cart.getRooms().remove((int) deleteIndex);
            }
        } else {
            // ➕ Add new item as before
            if (roomFields.getStandingString() != null) {
                Standing standing = standingService.findStandingByName(roomFields.getStandingString());
                roomFields.setStanding(standing);
            }

            if (roomFields.getAdditionalBonuses() != null) {
                List<RoomBonusEnum> enumBonuses = new ArrayList<>();
                for (RoomBonusEnum bonus : roomFields.getAdditionalBonuses()) {
                    try {
                        enumBonuses.add(RoomBonusEnum.valueOf(bonus.name()));
                    } catch (IllegalArgumentException ignored) {}
                }
                roomFields.setAdditionalBonuses(enumBonuses);
            }

            cart.getRooms().add(roomFields);
        }

        return "redirect:/cart";
    }
}
