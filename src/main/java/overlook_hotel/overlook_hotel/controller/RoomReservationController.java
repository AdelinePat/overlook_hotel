package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
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
    public String reservation(@ModelAttribute RoomReservationFields filterFields,
                              Model model) {
        System.out.println("\n\n\n\n\n\n\n\t\t\t\t\t\t\tpricerange: " + filterFields.getPriceRange());

        model.addAttribute("standingList", standingService.getFullStandingList());
        model.addAttribute("bedTypeList", BedType.values());

        List<Room> rooms = roomService.findAllFiltered(
                filterFields.getRoomNumber(),
                filterFields.getCapacity(),
                filterFields.getDescription(),
                filterFields.getStanding(),
                filterFields.getBedType(),
                filterFields.getStartDate(),
                filterFields.getEndDate(),
                filterFields.getPriceRange(),
                filterFields.getBonuses());

        model.addAttribute("filterFields", filterFields);
        model.addAttribute("roomList", rooms);
        model.addAttribute("title", "Réservation des chambres");
        model.addAttribute("titlePage", "Overlook Hotel - Réservation");
        model.addAttribute("bonusList", RoomBonusEnum.values());

        return "reservation";
    }

}
