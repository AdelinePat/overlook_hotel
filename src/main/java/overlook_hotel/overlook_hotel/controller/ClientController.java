package overlook_hotel.overlook_hotel.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import overlook_hotel.overlook_hotel.model.ClientFilter;
import overlook_hotel.overlook_hotel.model.entity.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import overlook_hotel.overlook_hotel.service.ClientService;

import java.util.List;

import static org.hibernate.validator.internal.util.ReflectionHelper.typeOf;

@Controller
public class ClientController {

    @GetMapping("/")
        public String index(Model model) {
        return "index";
    }




}
