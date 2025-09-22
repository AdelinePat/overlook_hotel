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

    /** LISTE + FILTRES */
    @RequestMapping(value = "/event-reservation", method = {RequestMethod.GET, RequestMethod.POST})
    public String reservation(@ModelAttribute EventFilterFields filterFields,
                              Model model,
                              HttpSession session) {

        // données pour les selects
        List<PlaceType> placeTypes = placeTypeService.getAll();
        model.addAttribute("placeTypeList", placeTypes);
        model.addAttribute("eventTypes", EventType.values());

        // liste filtrée
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

        // Panier : init si absent
        if (session.getAttribute("eventCart") == null) {
            session.setAttribute("eventCart", new ArrayList<Place>());
        }

        return "event-reservation";
    }

    /** AJOUT AU PANIER (simple, sans calculs serveur) */
    @PostMapping("/event-reservation/{id}")
    public String addToCart(@PathVariable Long id,
                            @ModelAttribute EventFilterFields filterFields,
                            Model model,
                            HttpSession session) {

        Place selectedPlace = placeService.findById(id);

        // Récupérer panier existant
        List<Place> cart = (List<Place>) session.getAttribute("eventCart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Ajouter la salle
        cart.add(selectedPlace);
        session.setAttribute("eventCart", cart);

        // Recharger liste + panier
        return reservation(filterFields, model, session);
    }
}
