package overlook_hotel.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import overlook_hotel.overlook_hotel.model.FilterFields;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.service.EmployeeService;
import overlook_hotel.overlook_hotel.model.entity.Job;
import overlook_hotel.overlook_hotel.service.JobService;
import overlook_hotel.overlook_hotel.util.InputSanitizer;
import overlook_hotel.overlook_hotel.util.InputValidator;

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
    @RequestParam(required = false) String password,
    @RequestParam(required = false) String confirmPassword,
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
                this.populateFilterFields(
                    InputSanitizer.sanitize(lastname),
                    InputSanitizer.sanitize(firstname),
                    InputSanitizer.sanitize(email),
                    job
                );
            } else if (action.equals("add") && id == null) {
                String validationError = validateFieldInput(lastname, firstname, email, job);
                if (validationError != null) {
                    model.addAttribute("error", validationError);
                    List<Employee> employees = employeeService.findAllFiltered(
                        this.filterFields.getLastname(),
                        this.filterFields.getFirstname(),
                        this.filterFields.getEmail(),
                        this.filterFields.getJob()
                    );
                    this.populateModel(model, employees, "employee", List.of("Nom", "Prénom", "Email", "Job"), jobService.getFullJobList());
                    return "table";
                }
                    // Password validation and hashing (factorized)
                    overlook_hotel.overlook_hotel.util.PasswordUtils.PasswordResult pwResult = overlook_hotel.overlook_hotel.util.PasswordUtils.validateAndHash(password, confirmPassword);
                    if (pwResult.error != null) {
                        model.addAttribute("error", pwResult.error);
                        List<Employee> employees = employeeService.findAllFiltered(
                            this.filterFields.getLastname(),
                            this.filterFields.getFirstname(),
                            this.filterFields.getEmail(),
                            this.filterFields.getJob()
                        );
                        // model.addAttribute("columns", List.of("Nom", "Prénom", "Email", "Job"));
                        // model.addAttribute("rows", employees);

                        this.populateModel(model, employees, "employee", List.of("Nom", "Prénom", "Email", "Job"), jobService.getFullJobList());
                        return "table";
                    }
                    try {
                        Employee newEmployee = new Employee();
                        newEmployee.setLastname(InputSanitizer.sanitize(lastname));
                        newEmployee.setFirstname(InputSanitizer.sanitize(firstname));
                        newEmployee.setEmail(InputSanitizer.sanitize(email));
                        newEmployee.setJob(job);
                        newEmployee.setPassword(pwResult.hashedPassword);
                        newEmployee.setSalt(pwResult.salt);
                        employeeService.save(newEmployee);
                        model.addAttribute("message", "Ajout réussi !");
                    } catch (Exception e) {
                        model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
                    }
                this.resetFocusedField(f -> new FilterFields());
                this.filterFields = new FilterFields();
            } else if (action.equals("update") && id != null) {
                String validationError = validateFieldInput(lastname, firstname, email, job);
                if (validationError != null) {
                    model.addAttribute("error", validationError);
                    List<Employee> employees = employeeService.findAllFiltered(
                        this.filterFields.getLastname(),
                        this.filterFields.getFirstname(),
                        this.filterFields.getEmail(),
                        this.filterFields.getJob()
                    );
                    this.populateModel(model, employees, "employee", List.of("Nom", "Prénom", "Email", "Job"), jobService.getFullJobList());
                    return "table";
                }
                try {
                    Employee employeeToUpdate = employeeService.findById(id);
                    if (employeeToUpdate != null) {
                        employeeToUpdate.setLastname(InputSanitizer.sanitize(lastname));
                        employeeToUpdate.setFirstname(InputSanitizer.sanitize(firstname));
                        employeeToUpdate.setEmail(InputSanitizer.sanitize(email));
                        employeeToUpdate.setJob(job);
                        employeeService.save(employeeToUpdate);
                        model.addAttribute("message", "Modification réussie !");
                    }
                } catch (Exception e) {
                    model.addAttribute("error", "Erreur lors de la modification : " + e.getMessage());
                }
                this.resetFocusedField(f -> new FilterFields());
                this.filterFields = new FilterFields();
            } else if (action.equals("delete") && id != null) {
                try {
                    employeeService.deleteById(id);
                    model.addAttribute("message", "Suppression réussie !");
                } catch (Exception e) {
                    model.addAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
                }
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
                FilterFields focusedField = new FilterFields();
                focusedField.setId(employee.getId().longValue());
                focusedField.setLastname(employee.getLastname());
                focusedField.setFirstname(employee.getFirstname());
                focusedField.setEmail(employee.getEmail());
                focusedField.setJob(employee.getJob());
                return focusedField;
            },
            focusedField -> new FilterFields()
        );

        this.populateModel(model, employees, "employee", List.of("Nom", "Prénom", "Email", "Job"), jobService.getFullJobList());

        return "table";
    }

        /**
     * Validates employee input fields. Returns null if all valid, otherwise error message.
     */
    private String validateFieldInput(String lastname, String firstname, String email, Job job) {
        if (!InputValidator.isValidWord(lastname)) {
            return "Nom invalide.";
        }
        if (!InputValidator.isValidWord(firstname)) {
            return "Prénom invalide.";
        }
        if (!InputValidator.isValidEmail(email)) {
            return "Email invalide.";
        }
        if (job == null) {
            return "Job invalide.";
        }
        return null;
    }

    private void populateFilterFields(String lastname, String firstname, String email, Job job) {
        if (lastname != null) this.filterFields.setLastname(lastname.trim());
        if (firstname != null) this.filterFields.setFirstname(firstname.trim());
        if (email != null) this.filterFields.setEmail(email.toLowerCase().trim());
        if (job != null) this.filterFields.setJob(job);
    }
}
