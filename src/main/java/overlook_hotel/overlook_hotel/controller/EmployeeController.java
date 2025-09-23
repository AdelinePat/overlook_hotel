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
public class EmployeeController extends AbstractEntityController<Employee, FilterFields> {
    private final EmployeeService employeeService;
    private final JobService jobService;
    private FilterFields filterFields;

    public EmployeeController(EmployeeService employeeService, JobService jobService) {
        this.employeeService = employeeService;
        this.jobService = jobService;
        this.filterFields = new FilterFields();
        this.focusedField = new FilterFields();
        this.focusedEntity = null;
    }


    @RequestMapping(value = "/employees", method = {RequestMethod.GET, RequestMethod.POST})
    public String employees(
        @RequestParam(required = false) String lastname,
        @RequestParam(required = false) String firstname,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) Long jobParam,
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) Boolean reset,
        @RequestParam(required = false) String action,
        Model model) {

        // Handle reset button
        if (reset != null && reset) {
            this.resetFocusedField(f -> new FilterFields());
            this.filterFields = new FilterFields();
            List<Employee> employees = employeeService.findAllFiltered("", "", "", null);
            this.populateModel(model, employees, "employee", List.of("Nom", "Prénom", "Email", "Job"), jobService.getFullJobList());
            this.focusedEntity = null;
            return "table";
        }

        // ---- CRUD ACTIONS ----
        Job job = null;
        if (jobParam != null) {
            job = jobService.findJobById(jobParam);
        }

        if (action != null) {
            if (action.equals("search")) {
                this.populateFilterFields(lastname, firstname, email, job);
            } else if (action.equals("add") && id == null) {
                Employee newEmployee = new Employee();
                newEmployee.setLastname(lastname);
                newEmployee.setFirstname(firstname);
                newEmployee.setEmail(email);
                newEmployee.setJob(job);
                newEmployee.setSalt("defaultSalt");
                newEmployee.setPassword("defaultPassword");
                employeeService.save(newEmployee);
                this.resetFocusedField(f -> new FilterFields());
                this.filterFields = new FilterFields();
            } else if (action.equals("update") && id != null) {
                Employee employeeToUpdate = employeeService.findById(id);
                if (employeeToUpdate != null) {
                    employeeToUpdate.setLastname(lastname);
                    employeeToUpdate.setFirstname(firstname);
                    employeeToUpdate.setEmail(email);
                    employeeToUpdate.setJob(job);
                    employeeService.save(employeeToUpdate);
                    this.resetFocusedField(f -> new FilterFields());
                }
                this.filterFields = new FilterFields();
            } else if (action.equals("delete") && id != null) {
                employeeService.deleteById(id);
                this.resetFocusedField(f -> new FilterFields());
                this.filterFields = new FilterFields();
            }
        }

        // ---- FILTRE ----
        lastname = lastname == null ? "" : lastname.trim();
        firstname = firstname == null ? "" : firstname.trim();
        email = email == null ? "" : email.toLowerCase().trim();

        List<Employee> employees = employeeService.findAllFiltered(
            this.filterFields.getLastname(),
            this.filterFields.getFirstname(),
            this.filterFields.getEmail(),
            this.filterFields.getJob()
        );

        // ---- FOCUS ----
        this.populateFocusField(id, 
            employeeService::findById,
            employee -> {
                FilterFields filter = new FilterFields();
                filter.setId(employee.getId().longValue());
                filter.setLastname(employee.getLastname());
                filter.setFirstname(employee.getFirstname());
                filter.setEmail(employee.getEmail());
                filter.setJob(employee.getJob());
                return filter;
            },
            f -> new FilterFields()
        );

        this.populateModel(model, employees, "employee", List.of("Nom", "Prénom", "Email", "Job"), jobService.getFullJobList());

        return "table";
    }

    private void populateFilterFields(String lastname, String firstname, String email, Job job) {
        if (lastname != null) this.filterFields.setLastname(lastname.trim());
        if (firstname != null) this.filterFields.setFirstname(firstname.trim());
        if (email != null) this.filterFields.setEmail(email.toLowerCase().trim());
        if (job != null) this.filterFields.setJob(job);
    }
}
