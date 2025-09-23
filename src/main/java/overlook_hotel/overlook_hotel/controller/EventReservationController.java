package overlook_hotel.overlook_hotel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.EventFilterFields;
import overlook_hotel.overlook_hotel.model.entity.Place;
import overlook_hotel.overlook_hotel.model.entity.PlaceType;
import overlook_hotel.overlook_hotel.model.enumList.EventType;
import overlook_hotel.overlook_hotel.service.PlaceService;
import overlook_hotel.overlook_hotel.service.PlaceTypeService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EventReservationController {

    private final PlaceService placeService;
    private final PlaceTypeService placeTypeService;

    public EventReservationController(PlaceService placeService, PlaceTypeService placeTypeService) {
        this.placeService = placeService;
        this.placeTypeService = placeTypeService;
    }

    /** LIST + FILTERS */
    @RequestMapping(value = "/event-reservation", method = {RequestMethod.GET, RequestMethod.POST})
    public String reservation(@ModelAttribute EventFilterFields filterFields,
                              Model model,
                              HttpSession session) {

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
}
