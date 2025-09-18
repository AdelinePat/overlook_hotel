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
    private FilterFields filterFields;
    private FilterFields focusedField;
    private Employee focusedEmployee;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.filterFields = new FilterFields();
        this.focusedField = new FilterFields();
        this.focusedEmployee = new Employee();
    }

    // @RequestMapping(value = "/employees", method = {RequestMethod.GET, RequestMethod.POST})
    // public String employees(@RequestParam(required = false) String lastname,
    //                         @RequestParam(required = false) String firstname,
    //                         @RequestParam(required = false) String email,
    //                         @RequestParam(required = false) Job job,
    //                         Model model) {

    //     model.addAttribute("testBlabla", "les gens. coucou florence et thibault");

    //     lastname = lastname == null ? "" : lastname.trim();
    //     firstname = firstname == null ? "" : firstname.trim();
    //     email = email == null ? "" : email.toLowerCase().trim();

    //     FilterFields employeeFilter = new FilterFields();
    //     model.addAttribute("filterField", employeeFilter);
    //     List<Employee> employees = employeeService.findAllFiltered(lastname, firstname, email, job);

    //     employeeFilter.setLastname(lastname);
    //     employeeFilter.setFirstname(firstname);
    //     employeeFilter.setEmail(email);
    //     employeeFilter.setJob(job);

    //     model.addAttribute("clients", employees);

    //     model.addAttribute("title", "Employés");
    //     model.addAttribute("columns", List.of("ID", "Nom", "Prénom", "Email", "Job"));
    //     model.addAttribute("rows", employees);
    //     model.addAttribute("entityType", "employee");

    //     return "table";
    // }

     @RequestMapping(value = "/employees", method = {RequestMethod.GET, RequestMethod.POST})
    public String employees(
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String jobParam,
            @RequestParam(required = false) Long id,
            Model model) {

        // ---- FILTRE ----
        lastname = lastname == null ? "" : lastname.trim();
        firstname = firstname == null ? "" : firstname.trim();
        email = email == null ? "" : email.toLowerCase().trim();
        Job job = (jobParam == null || jobParam.isBlank()) ? null : Job.valueOf(jobParam);
        id = id == null ? null : id;

        this.populateFilterFields(lastname, firstname, email, job);

        List<Employee> employees = employeeService.findAllFiltered(
            this.filterFields.getLastname(),
            this.filterFields.getFirstname(),
            this.filterFields.getEmail(),
            this.filterFields.getJob()
            );

        
        // ---- FOCUS ----
        this.populateFocusField(id);
        if (this.focusedField.getId() != null) {
            this.focusedEmployee = employeeService.findById(this.focusedField.getId());
        } else {
            this.resetFocusedField();
        }

        // ---- MODEL ----
        model.addAttribute("clients", employees);
        model.addAttribute("focusedClient", focusedEmployee);
        model.addAttribute("focusField", this.focusedField);
        model.addAttribute("filterField", this.filterFields);
        model.addAttribute("title", "Clients");
        model.addAttribute("columns", List.of("ID", "Nom", "Prénom", "Email", "Job"));
        model.addAttribute("rows", employees);
        model.addAttribute("entityType", "employee");

        return "table";
    }

    private void populateFilterFields(String lastname, String firstname, String email, Job job) {
        if (lastname != null) this.filterFields.setLastname(lastname.trim());
        if (firstname != null) this.filterFields.setFirstname(firstname.trim());
        if (email != null) this.filterFields.setEmail(email.toLowerCase().trim());
        if (job != null) this.filterFields.setJob(job);
    }

    private void resetFocusedField() {
        this.focusedField.setId(null);
        this.focusedField.setLastname(null);
        this.focusedField.setFirstname(null);
        this.focusedField.setEmail(null);
        this.focusedField.setPhone(null);
        this.focusedField.setPassword(null);
        this.focusedField.setJob(null);
        this.focusedField.setPriceRange(null);
    }

    private void populateFocusField(Long id) {
        if (id != null) {
            this.focusedField.setId(id);
            this.focusedEmployee = employeeService.findById(id);

            if (this.focusedEmployee != null) {
                this.focusedField.setId(this.focusedEmployee.getId().longValue());
                this.focusedField.setLastname(this.focusedEmployee.getLastname());
                this.focusedField.setFirstname(this.focusedEmployee.getFirstname());
                this.focusedField.setEmail(this.focusedEmployee.getEmail());
                this.focusedField.setJob(this.focusedEmployee.getJob());
            } else {
                this.resetFocusedField();
            }
        } else {
            this.resetFocusedField();
        }
    }
}
