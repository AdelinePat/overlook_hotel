package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.FilterFields;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.service.RoomService;
import overlook_hotel.overlook_hotel.service.StandingService;

import java.util.List;

@Controller
public class RoomReservationController {
    private final RoomService roomService;
    private final StandingService standingService;

    public RoomReservationController(RoomService roomService,StandingService standingService) {
        this.roomService = roomService;
        this.standingService = standingService;
    }

    @RequestMapping(value = "/room-reservation", method = {RequestMethod.GET, RequestMethod.POST})
    public String reservation(@ModelAttribute FilterFields filterfields,
                              Model model) {
        model.addAttribute("standingList", standingService.getFullStandingList());
        model.addAttribute("bedTypeList", BedType.values());

        List<Room> rooms = roomService.findAllFiltered(
                filterfields.getRoomNumber(),
                filterfields.getCapacity(),
                filterfields.getDescription(),
                filterfields.getStanding(),
                filterfields.getBedType(),
                filterfields.getStartDate(),
                filterfields.getEndDate(),
                filterfields.getPriceRange());

        model.addAttribute("filterFields", filterfields);
        model.addAttribute("roomList", rooms);
        model.addAttribute("title", "Réservation des chambres");
        model.addAttribute("titlePage", "Overlook Hotel - Réservation");

        System.out.println("\n\n\n\n\t\t\t\t\t\t\t\t\tPOOOOST Filter called with: " + filterfields + "\n\n\n\n");
        System.out.println("number: " + filterfields.getRoomNumber());
        System.out.println("capacity: " + filterfields.getCapacity());
        System.out.println("description: " + filterfields.getDescription());
        System.out.println("standing: " + filterfields.getStanding());
        System.out.println("bedtype: " + filterfields.getBedType());
        System.out.println("startdate: " + filterfields.getStartDate());
        System.out.println("endDate: " + filterfields.getEndDate());
        System.out.println("priceRange: " + filterfields.getPriceRange());
        for (Room room: rooms) {
            System.out.println(
                    room.getNumber() + " " + room.getDescription()
            );
        }

        System.out.println("fin de boucle ?");
        return "reservation";
    }

}
