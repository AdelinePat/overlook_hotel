package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.FilterFields;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.service.EmployeeService;
import overlook_hotel.overlook_hotel.model.entity.Job;
import overlook_hotel.overlook_hotel.service.JobService;


import java.util.List;

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;
    private final JobService jobService;
    private FilterFields filterFields;
    private FilterFields focusedField;
    private Employee focusedEmployee;

    public EmployeeController(EmployeeService employeeService, JobService jobService) {
        this.employeeService = employeeService;
        this.jobService = jobService;
        this.filterFields = new FilterFields();
        this.focusedField = new FilterFields();
        this.focusedEmployee = new Employee();
    }


     @RequestMapping(value = "/employees", method = {RequestMethod.GET, RequestMethod.POST})
    public String employees(
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String jobParam,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Boolean reset,
            @RequestParam(required = false) String action,
            Model model) {

        // Handle reset button
        if (reset != null && reset) {
            this.resetFocusedField();
            this.filterFields = new FilterFields();
            List<Employee> employees = employeeService.findAllFiltered("", "", "", null);
            model.addAttribute("clients", employees);
            model.addAttribute("focusedClient", null);
            model.addAttribute("focusField", new FilterFields());
            model.addAttribute("filterField", new FilterFields());
            model.addAttribute("title", "Employés");
            model.addAttribute("titlePage", "Gestion des employés");
            model.addAttribute("columns", List.of("Nom", "Prénom", "Email", "Job"));
            model.addAttribute("rows", employees);
            model.addAttribute("entityType", "employee");
            model.addAttribute("jobEnumValues", jobService.getFullJobList());
            this.focusedEmployee = null;
            return "table";
        }

        // ---- CRUD ACTIONS ----
        Job job = null;
        Long jobId = null;
        if (jobParam != null && !jobParam.isBlank()) {
            try {
                jobId = Long.parseLong(jobParam);
            } catch (NumberFormatException ignored) {}
        }
        if (jobId != null) {
            job = jobService.findJobById(jobId);
        }

        if (action != null) {
            if (action.equals("add")) {
                Employee newEmployee = new Employee();
                newEmployee.setLastname(lastname);
                newEmployee.setFirstname(firstname);
                newEmployee.setEmail(email);
                newEmployee.setJob(job);
                newEmployee.setSalt("defaultSalt");
                newEmployee.setPassword("defaultPassword");
                employeeService.save(newEmployee);
                this.resetFocusedField();
                this.filterFields = new FilterFields();
            } else if (action.equals("update") && id != null) {
                Employee employeeToUpdate = employeeService.findById(id);
                if (employeeToUpdate != null) {
                    employeeToUpdate.setLastname(lastname);
                    employeeToUpdate.setFirstname(firstname);
                    employeeToUpdate.setEmail(email);
                    employeeToUpdate.setJob(job);
                    employeeService.save(employeeToUpdate);
                    this.resetFocusedField();
                    this.filterFields = new FilterFields();
                }
            } else if (action.equals("delete") && id != null) {
                employeeService.deleteById(id);
                this.resetFocusedField();
                this.filterFields = new FilterFields();
            }
        }

        // ---- FILTRE ----
        lastname = lastname == null ? "" : lastname.trim();
        firstname = firstname == null ? "" : firstname.trim();
        email = email == null ? "" : email.toLowerCase().trim();

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
        model.addAttribute("title", "Employés");
        model.addAttribute("titlePage", "Gestion des employés");
        model.addAttribute("columns", List.of("Nom", "Prénom", "Email", "Job"));
        model.addAttribute("rows", employees);
        model.addAttribute("entityType", "employee");
        model.addAttribute("jobEnumValues", jobService.getFullJobList());

        return "table";
    }

    private void populateFilterFields(String lastname, String firstname, String email, Job job) {
        if (lastname != null) this.filterFields.setLastname(lastname.trim());
        if (firstname != null) this.filterFields.setFirstname(firstname.trim());
        if (email != null) this.filterFields.setEmail(email.toLowerCase().trim());
//        if (job != null) this.filterFields.setJob(job);
        this.filterFields.setJob(job);
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
                this.focusedField.setId(this.focusedEmployee.getId());
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
