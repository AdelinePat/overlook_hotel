package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.RoomReservationCart;
import overlook_hotel.overlook_hotel.model.RoomReservationFields;
import overlook_hotel.overlook_hotel.model.entity.Feedback;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.RoomBonus;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.service.RoomService;
import overlook_hotel.overlook_hotel.service.StandingService;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@SessionAttributes({"roomReservationFilter", "roomReservationCart"})
public class RoomReservationController {
    private final RoomService roomService;
    private final StandingService standingService;

    public RoomReservationController(RoomService roomService,StandingService standingService) {
        this.roomService = roomService;
        this.standingService = standingService;
    }

    @ModelAttribute("roomReservationFilter")
    public RoomReservationFields createFilter() {
        RoomReservationFields filter = new RoomReservationFields();
        if (filter.getStartDate() == null) filter.setStartDate(LocalDate.now());
        if (filter.getEndDate() == null) filter.setEndDate(LocalDate.now().plusDays(1));
        return filter;
    }

    @ModelAttribute("roomReservationCart")
    public RoomReservationCart createCart() {
        RoomReservationCart cart = new RoomReservationCart();
        cart.setRooms(new ArrayList<>());
        return cart;
    }

    @RequestMapping(value = "/room-reservation", method = {RequestMethod.GET, RequestMethod.POST})
    public String reservation(@ModelAttribute("roomReservationFilter") RoomReservationFields filterFields,
                              @ModelAttribute("roomReservationCart") RoomReservationCart cart,
                              @RequestParam(value = "resetFilter", required = false) String resetFilter,
                              Model model) {


        List<String> errors = new ArrayList<>();

        this.generateMessage(errors, filterFields);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
        }

        if ("true".equals(resetFilter)) {
            this.resetFilters(filterFields);
        }

        boolean cartHasRooms = cart.getRooms() != null && !cart.getRooms().isEmpty();

        if (cartHasRooms) {
            RoomReservationFields firstRoom = cart.getRooms().get(0);
            filterFields.setStartDate(firstRoom.getStartDate());
            filterFields.setEndDate(firstRoom.getEndDate());
        } else if (filterFields.getStartDate() == null || filterFields.getEndDate() == null) {
                filterFields.setStartDate(LocalDate.now());
                filterFields.setEndDate(LocalDate.now().plusDays(1));
        }

        List<Long> excludedRoomIds = cart.getRooms().stream()
                .map(RoomReservationFields::getIdRoom)
                .toList();

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
                filterFields.getBonuses(),
                excludedRoomIds);

        model.addAttribute("filterFields", filterFields);
        model.addAttribute("roomList", rooms);
        model.addAttribute("title", "Réservation des chambres");
        model.addAttribute("titlePage", "Overlook Hotel - Réservation");
        model.addAttribute("bonusList", RoomBonusEnum.values());

        model.addAttribute("lockDateInputs", cartHasRooms);

        return "reservation";
    }


    @RequestMapping(value = "/room-reservation/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String roomDetails(@PathVariable Long id,
                              @RequestParam(required = false) List<String> selectedBonuses,
                              @ModelAttribute RoomReservationFields filterFields,
                              Model model) {
        Room room = roomService.findById(id);
        model.addAttribute("room", room);


        filterFields.setIdRoom(room.getId());
        filterFields.setRoomNumber(room.getNumber());
        filterFields.setCapacity(room.getCapacity());
        filterFields.setDescription(room.getDescription());
        filterFields.setStandingString(room.getStanding().getName());

        model.addAttribute("startDate", filterFields.getStartDate());
        model.addAttribute("endDate", filterFields.getEndDate());

        model.addAttribute("roomReservationFields", filterFields); // for form
        int nights = 0;
        if (filterFields.getStartDate() != null &&  filterFields.getEndDate() != null) {
            nights = (int) ChronoUnit.DAYS.between(filterFields.getStartDate(), filterFields.getEndDate());
            if (nights < 0) nights = 0;
        }
        model.addAttribute("nights", nights);

        BigDecimal baseTotalPerNight = room.getTotalNightPrice();

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

    private void resetFilters(RoomReservationFields filterFields) {
        filterFields.setStartDate(LocalDate.now());
        filterFields.setEndDate(LocalDate.now().plusDays(1));

        filterFields.setRoomNumber(null);
        filterFields.setCapacity(null);
        filterFields.setDescription(null);
        filterFields.setStanding(null);
        filterFields.setBedType(null);
        filterFields.setBonuses(null);

        filterFields.setPriceRange(Arrays.asList(null, null));

        filterFields.setAdditionalBonuses(null);
    }

    private void generateMessage(List<String> errors, RoomReservationFields filterFields) {
        if (filterFields.getStartDate() != null && filterFields.getEndDate() != null) {
            if (filterFields.getStartDate().isAfter(filterFields.getEndDate()) ||
                    filterFields.getStartDate().isEqual(filterFields.getEndDate())) {
                errors.add("La date de début doit être avant la date de fin.");
            }
        }

        if (filterFields.getStartDate() != null &&
                filterFields.getStartDate().isBefore(LocalDate.now())) {
            errors.add("La date de début doit être aujourd'hui ou dans le futur.");
        }


        if (filterFields.getRoomNumber() != null && filterFields.getRoomNumber() <= 0) {
            errors.add("Le numéro de chambre doit être supérieur à 0.");
        }

        if (filterFields.getPriceRange() != null && filterFields.getPriceRange().size() == 2) {
            Integer minPrice = filterFields.getPriceRange().get(0);
            Integer maxPrice = filterFields.getPriceRange().get(1);

            if (minPrice != null && minPrice < 0) {
                errors.add("Le prix minimum doit être supérieur ou égal à 0.");
            }

            if (minPrice != null && maxPrice != null && maxPrice.compareTo(minPrice) < 0) {
                errors.add("Le prix maximum doit être supérieur ou égal au prix minimum.");
            }
        }
    }

}