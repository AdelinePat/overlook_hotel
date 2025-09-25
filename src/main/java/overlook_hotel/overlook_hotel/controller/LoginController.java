package overlook_hotel.overlook_hotel.controller;

import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.model.entity.Manager;
import overlook_hotel.overlook_hotel.service.ClientService;
import overlook_hotel.overlook_hotel.service.EmployeeService;
import overlook_hotel.overlook_hotel.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ManagerService managerService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("error", "");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String role,
                        Model model) {
        Object user = null;
        switch (role) {
            case "client":
                user = clientService.authenticate(email, password);
                break;
            case "employee":
                user = employeeService.authenticate(email, password);
                break;
            case "manager":
                user = managerService.authenticate(email, password);
                break;
        }
        if (user != null) {
            // TODO: Set session attributes as needed
            model.addAttribute("user", user);
            model.addAttribute("role", role);
            return "redirect:/"; // Redirect to home or dashboard
        } else {
            model.addAttribute("error", "Invalid credentials or role.");
            return "login";
        }
    }
}
