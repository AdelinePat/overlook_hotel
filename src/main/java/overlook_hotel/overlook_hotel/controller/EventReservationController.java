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

    @RequestMapping(value = "/event-reservation", method = {RequestMethod.GET, RequestMethod.POST})
    public String reservation(@ModelAttribute EventFilterFields filterFields,
                              Model model) {

        if (filterFields.getStartDate() == null) {
            filterFields.setStartDate(
                    LocalDateTime.now().plusDays(1).withMinute(0).withSecond(0).withNano(0)
            );
        }

        // pour les selects

        List<PlaceType> placeTypes = placeTypeService.getAll();
        model.addAttribute("placeTypeList", placeTypes);
        model.addAttribute("eventTypes", EventType.values());

        String placeTypeName = null;
        if(filterFields.getPlaceTypeId() != null) {
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

        //pour la vue
        model.addAttribute("filterFields", filterFields);
        model.addAttribute("placeList", places);
        model.addAttribute("title", "Réservation d'événement");
        model.addAttribute("titlePage", "Overlook Hotel - Réservation événement");

        //pring de log

        for (Place p : places) {
            System.out.println("Place #" + p.getId() + " cap=" + p.getCapacity());
        }

        return "event-reservation";

    }
}
