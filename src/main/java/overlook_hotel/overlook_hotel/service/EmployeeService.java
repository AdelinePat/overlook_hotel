package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.specification.EmployeeSpecification;
import overlook_hotel.overlook_hotel.model.enumList.Job;
import overlook_hotel.overlook_hotel.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAllFiltered(String lastname,
                                          String firstname,
                                          String email,
                                          Job job) {

        Specification<Employee> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (lastname != null && !lastname.isBlank()) {
            spec = spec.and(EmployeeSpecification.hasLastname(lastname));
        }
        if (firstname != null && !firstname.isBlank()) {
            spec = spec.and(EmployeeSpecification.hasFirstname(firstname));
        }
        if (email != null && !email.isBlank()) {
            spec = spec.and(EmployeeSpecification.hasEmail(email));
        }
        if (job != null) {
            spec = spec.and(EmployeeSpecification.hasJob(job));
        }

        return employeeRepository.findAll(spec);
    }
}
