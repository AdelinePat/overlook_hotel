package overlook_hotel.overlook_hotel.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.RoomReservationCart;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;
import overlook_hotel.overlook_hotel.service.RoomService;

import java.util.ArrayList;
@Controller
public class CartController {

    private RoomService roomService;
//    private RoomReservationService roomReservationService;

    @ModelAttribute("roomReservationCart")
    public RoomReservationCart createCart() {
        RoomReservationCart cart = new RoomReservationCart();
        cart.setRooms(new ArrayList<>());
        return cart;
    }

//    @RequestMapping(value = "/room-reservation/{id}/cart", method = {RequestMethod.GET, RequestMethod.POST})
//    public String reservation(@PathVariable Long id,
//                              @ModelAttribute RoomReservationFields fields,
//                              @ModelAttribute("roomReservationCart") RoomReservationCart cart,
//                              Model model) {
//        // Add the room reservation to the cart
//        fields.setId(id); // set the room ID manually since it comes from path
//        cart.getRooms().add(fields);
//        model.addAttribute("cartItems", cart.getRooms());
//
//        // Redirect to cart page
//        return "redirect:/cart";
//
//    }

    @PostMapping("/room-reservation/{id}/cart")
    public String addRoomToCart(@PathVariable Long id,
                                @ModelAttribute RoomReservationFields fields,
                                @ModelAttribute("roomReservationCart") RoomReservationCart cart) {
        // Add the room reservation to the cart
        fields.setId(id); // set the room ID manually since it comes from path
        cart.getRooms().add(fields);

        // Redirect to cart page
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(@ModelAttribute("roomReservationCart") RoomReservationCart cart,
                           Model model) {
        model.addAttribute("cartItems", cart.getRooms());
        return "cart"; // Thymeleaf template
    }


    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("roomReservationCart") RoomReservationCart cart) {
        for (RoomReservationFields fields : cart.getRooms()) {
            Room room = roomService.findById(fields.getId());
            RoomReservation reservation = new RoomReservation();
            reservation.setStartDate(fields.getStartDate());
            reservation.setEndDate(fields.getEndDate());
            reservation.setStatus(false);
            // ... save bonuses as RoomReservationBonus entities ...
//            roomReservationService.save(reservation, room, fields.getAdditionalBonuses());
        }
        cart.getRooms().clear(); // empty cart after booking
        return "redirect:/confirmation";
    }


}
