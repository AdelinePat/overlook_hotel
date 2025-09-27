package overlook_hotel.overlook_hotel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import overlook_hotel.overlook_hotel.model.EventFilterFields;
import overlook_hotel.overlook_hotel.model.entity.*;
import overlook_hotel.overlook_hotel.model.enumList.EventType;
import overlook_hotel.overlook_hotel.repository.ClientRepository;
import overlook_hotel.overlook_hotel.repository.EventLinkPlaceRepository;
import overlook_hotel.overlook_hotel.repository.EventReservationRepository;
import overlook_hotel.overlook_hotel.service.PlaceService;
import overlook_hotel.overlook_hotel.service.PlaceTypeService;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

@Controller
public class EventReservationController {

    private final PlaceService placeService;
    private final PlaceTypeService placeTypeService;
    private final EventReservationRepository eventReservationRepository;
    private final EventLinkPlaceRepository eventLinkPlaceRepository;
    private final ClientRepository clientRepository;

    public EventReservationController(PlaceService placeService, PlaceTypeService placeTypeService, EventReservationRepository eventReservationRepository, EventLinkPlaceRepository eventLinkPlaceRepository, ClientRepository clientRepository) {
        this.placeService = placeService;
        this.placeTypeService = placeTypeService;
        this.eventReservationRepository = eventReservationRepository;
        this.eventLinkPlaceRepository = eventLinkPlaceRepository;
        this.clientRepository = clientRepository;
    }

    /** LIST + FILTERS */
    @RequestMapping(value = "/event-reservation", method = {RequestMethod.GET, RequestMethod.POST})
    public String reservation(@ModelAttribute EventFilterFields filterFields,
                              Model model,
                              HttpSession session) {

        LocalDateTime tomorrow = LocalDate.now()
                .plusDays(1)
                .atTime(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));



        if (filterFields.getStartDate() == null) {
            filterFields.setStartDate(tomorrow);
        }
        if (filterFields.getEndDate() == null) {
            filterFields.setEndDate(filterFields.getStartDate().plusHours(1));
        }


        if (filterFields.getStartDate().isBefore(tomorrow)) {
            filterFields.setStartDate(tomorrow);
            filterFields.setEndDate(tomorrow.plusHours(1));
        }


        if (!filterFields.getEndDate().isAfter(filterFields.getStartDate())) {
            model.addAttribute("errorMessage", "La date de fin doit être postérieure à la date de début.");
            filterFields.setEndDate(filterFields.getStartDate().plusHours(1));
        }


        // data for select inputs
        List<PlaceType> placeTypes = placeTypeService.getAll();
        model.addAttribute("placeTypeList", placeTypes);
        model.addAttribute("eventTypes", EventType.values());

        // filtered list
        List<Place> places = placeService.findAllFiltered(
                filterFields.getPlaceTypeId() != null ? placeTypeService.findById(filterFields.getPlaceTypeId()).getName() : null,
                filterFields.getMinCapacity(),
                filterFields.getStartDate(),
                filterFields.getEndDate(),
                null,
                null
        );

        model.addAttribute("filterFields", filterFields);
        model.addAttribute("placeList", places);
        model.addAttribute("title", "Réservation d'événement");
        model.addAttribute("titlePage", "Overlook Hotel - Réservation événement");

        // Cart: initialize if absent
        if (session.getAttribute("eventCart") == null) {
            session.setAttribute("eventCart", new ArrayList<Place>());
        }

        return "event-reservation";
    }

    /** ADD TO CART (simple, without server-side calculations) */
    @PostMapping("/event-reservation/{id}")
    public String addToCart(@PathVariable Long id,
                            @ModelAttribute EventFilterFields filterFields,
                            Model model,
                            HttpSession session) {

        Place selectedPlace = placeService.findById(id);

        // Retrieve existing cart
        List<Place> cart = (List<Place>) session.getAttribute("eventCart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Add the selected room
        cart.add(selectedPlace);
        session.setAttribute("eventCart", cart);

        // Reload list + cart
        return reservation(filterFields, model, session);
    }
    @PostMapping("/event-reservation/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        List<Place> cart = (List<Place>) session.getAttribute("eventCart");
        if(cart != null) {
            cart.removeIf(place -> place != null && place.getId() != null && place.getId().equals(id));
            session.setAttribute("eventCart", cart);

        }
        return "redirect:/event-reservation";
    }

    @PostMapping("/event-reservation/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("eventCart");
        return "redirect:/event-reservation";
    }

    @PostMapping("/event-reservation/confirm")
    public String confirmReservation(@ModelAttribute EventFilterFields filterFields,
                                     RedirectAttributes redirectAttributes,
                                     HttpSession session,
                                     Principal principal) {

        //user is connected
        if (principal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vous devez être connecté pour réserver.");
            return "redirect:/login";
        }

        String email = principal.getName();
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur inconnu");
            return "redirect:/login";
        }

        List<Place> cart = (List<Place>) session.getAttribute("eventCart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Votre panier est vide.");
            return "redirect:/event-reservation";
        }

        //
        LocalDateTime tomorrow = LocalDate.now()
                .plusDays(1)
                .atTime(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));

        LocalDateTime startDate = filterFields.getStartDate();
        if (startDate == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date de début est obligatoire.");
            return "redirect:/event-reservation";
        }
        if (startDate.isBefore(tomorrow)) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date de début doit être à partir de demain.");
            return "redirect:/event-reservation";
        }

        LocalDateTime endDate = filterFields.getEndDate() != null
                ? filterFields.getEndDate()
                : startDate.plusHours(1);

        if (!endDate.isAfter(startDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date de fin doit être postérieure à la date de début.");
            return "redirect:/event-reservation";
        }

        long hours = java.time.Duration.between(startDate, endDate).toHours();
        if (java.time.Duration.between(startDate, endDate).toMinutesPart() > 0) {
            hours++;
        }

        //
        BigDecimal total = BigDecimal.ZERO;
        for (Place place : cart) {
            total = total.add(place.getHourlyPrice().multiply(BigDecimal.valueOf(hours)));
        }

        EventType chosenEventType = filterFields.getEventType();
        if (chosenEventType == null) {
            chosenEventType = EventType.AUTRE;
        }


        EventReservation reservation = new EventReservation();
        reservation.setClient(client);
        reservation.setEventType(filterFields.getEventType());
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotalPrice(total);

        EventReservation savedReservation = eventReservationRepository.save(reservation);


        for (Place place : cart) {
            EventLinkReservation link = new EventLinkReservation();
            link.setEventReservation(savedReservation);
            link.setPlace(place);
            eventLinkPlaceRepository.save(link);
        }


        session.removeAttribute("eventCart");

        redirectAttributes.addFlashAttribute("successMessage", "Réservation enregistrée avec succès !");
        return "redirect:/event-reservation";
    }


    @GetMapping("/event-reservation/list")
    public String listReservations(Model model, Principal principal, RedirectAttributes redirectAttributes) {


        if (principal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vous devez être connecté pour voir vos réservations.");
            return "redirect:/login";
        }


        String email = principal.getName();
        Client client = clientRepository.findByEmail(email);

        if (client == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur inconnu.");
            return "redirect:/login";
        }


        List<EventReservation> reservations = eventReservationRepository.findByClientId(client.getId());

        model.addAttribute("reservations", reservations);
        model.addAttribute("titlePage", "Mes réservations - Overlook Hotel");

        return "event-reservation-list";
    }




}