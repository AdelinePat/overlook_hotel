package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.FilterFields;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.model.enumList.Job;
import overlook_hotel.overlook_hotel.service.ClientService;
import overlook_hotel.overlook_hotel.service.EmployeeService;

import java.util.List;

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(value = "/employees", method = {RequestMethod.GET, RequestMethod.POST})
    public String employees(@RequestParam(required = false) String lastname,
                            @RequestParam(required = false) String firstname,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) Job job,
                            Model model) {

        model.addAttribute("testBlabla", "les gens. coucou florence et thibault");

        lastname = lastname == null ? "" : lastname.trim();
        firstname = firstname == null ? "" : firstname.trim();
        email = email == null ? "" : email.toLowerCase().trim();

        FilterFields employeeFilter = new FilterFields();
        model.addAttribute("filterField", employeeFilter);
        List<Employee> employees = employeeService.findAllFiltered(lastname, firstname, email, job);

        employeeFilter.setLastname(lastname);
        employeeFilter.setFirstname(firstname);
        employeeFilter.setEmail(email);
        employeeFilter.setJob(job);

        model.addAttribute("clients", employees);

        model.addAttribute("title", "Employés");
        model.addAttribute("columns", List.of("ID", "Nom", "Prénom", "Email", "Job"));
        model.addAttribute("rows", employees);
        model.addAttribute("entityType", "employee");

        return "table";
    }
}