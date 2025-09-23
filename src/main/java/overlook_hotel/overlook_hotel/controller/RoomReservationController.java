package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.Feedback;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.RoomBonus;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.service.RoomService;
import overlook_hotel.overlook_hotel.service.StandingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

//    @GetMapping("/room-reservation/{id}")
    @RequestMapping(value = "/room-reservation/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String roomDetails(@PathVariable Long id,
                              @RequestParam(required = false) List<String> selectedBonuses,
                              @ModelAttribute RoomReservationFields filterFields,
                              Model model) {
        // 1. Get room by id
        Room room = roomService.findById(id);
        model.addAttribute("room", room);

        LocalDate startDate = filterFields.getStartDate();
        LocalDate endDate = filterFields.getEndDate();
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        int nights = 0;
        if (startDate != null && endDate != null) {
            nights = (int) ChronoUnit.DAYS.between(startDate, endDate);
            if (nights < 0) nights = 0;
        }
        model.addAttribute("nights", nights);

        BigDecimal baseTotalPerNight = room.getTotalNightPrice();

        // 2. Get default bonuses for the room
//        List<RoomBonusEnum> bonusList = List.of(RoomBonusEnum.values());
        List<RoomBonus> roomBonusList = roomService.getAllBonuses();

        List<RoomBonus> filteredBonuses = roomBonusList.stream()
                .filter(bonus -> room.getBonuses().stream()
                        .noneMatch(rb -> rb.getType() == bonus.getType()))
                .toList();

        model.addAttribute("roomBonusList", filteredBonuses);

        // 3. Get feedback for this room (through room_reservation -> feedback)
        List<Feedback> feedbackList = roomService.getRoomFeedback(id);
        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("totalPerNight", baseTotalPerNight);
        model.addAttribute("titlePage", "Détails de la chambre " + room.getNumber());
        return "room-detail";
    }
}
