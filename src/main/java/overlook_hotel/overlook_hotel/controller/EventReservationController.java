package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.EventFilterFields;
import overlook_hotel.overlook_hotel.model.entity.Place;
import overlook_hotel.overlook_hotel.model.entity.PlaceType;
import overlook_hotel.overlook_hotel.model.enumList.EventType;
import overlook_hotel.overlook_hotel.service.PlaceService;
import overlook_hotel.overlook_hotel.service.PlaceTypeService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
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
                              Model model) {

        if (filterFields.getStartDate() == null) {
            filterFields.setStartDate(LocalDateTime.now().plusDays(1).withMinute(0).withSecond(0).withNano(0));
        }

        // données pour les selects
        List<PlaceType> placeTypes = placeTypeService.getAll();
        model.addAttribute("placeTypeList", placeTypes);
        model.addAttribute("eventTypes", EventType.values());

        String placeTypeName = null;
        if (filterFields.getPlaceTypeId() != null) {
            PlaceType pt = placeTypeService.findById(filterFields.getPlaceTypeId());
            placeTypeName = (pt != null ? pt.getName() : null);
        }

        // liste filtrée
        List<Place> places = placeService.findAllFiltered(
                placeTypeName,
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

        return "event-reservation";
    }

    /** SELECTION D’UN ESPACE -> ON RESTE SUR event-reservation */
    @RequestMapping(value = "/event-reservation/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String selectPlace(@PathVariable Long id,
                              @ModelAttribute EventFilterFields filterFields,
                              Model model) {

        // Toujours recharger les listes et la liste filtrée (pour garder les cards + filtres)
        List<PlaceType> placeTypes = placeTypeService.getAll();
        model.addAttribute("placeTypeList", placeTypes);
        model.addAttribute("eventTypes", EventType.values());

        String placeTypeName = null;
        if (filterFields.getPlaceTypeId() != null) {
            PlaceType pt = placeTypeService.findById(filterFields.getPlaceTypeId());
            placeTypeName = (pt != null ? pt.getName() : null);
        }

        List<Place> places = placeService.findAllFiltered(
                placeTypeName,
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

        // Charger la salle sélectionnée
        Place selectedPlace = placeService.findById(id);
        model.addAttribute("selectedPlace", selectedPlace);

        // Dates + durée
        LocalDateTime start = filterFields.getStartDate();
        LocalDateTime end   = filterFields.getEndDate();

        // si end manquante, on met start + 2h (simple défaut pour afficher un total)
        if (start != null && end == null) {
            end = start.plusHours(2);
            filterFields.setEndDate(end);
        }

        long hours = 0;
        if (start != null && end != null && !end.isBefore(start)) {
            hours = Math.max(1, Duration.between(start, end).toHours()); // au moins 1h
        }

        model.addAttribute("hours", hours);

        // Total = prix horaire * nb d'heures
        BigDecimal hourly = selectedPlace.getHourlyPrice();
        BigDecimal total  = (hourly != null ? hourly.multiply(BigDecimal.valueOf(hours)) : BigDecimal.ZERO);
        model.addAttribute("totalPrice", total);

        // On reste sur la même vue
        return "event-reservation";
    }
}
