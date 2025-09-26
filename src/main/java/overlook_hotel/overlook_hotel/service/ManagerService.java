package overlook_hotel.overlook_hotel.service;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.Manager;
import overlook_hotel.overlook_hotel.specification.ManagerSpecification;
import overlook_hotel.overlook_hotel.repository.ManagerRepository;

import java.util.List;

@Service
public class ManagerService {
    private final PasswordEncoder passwordEncoder;
    public Manager authenticate(String email, String password) {
        Manager manager = managerRepository.findByEmail(email);
        if (manager != null && manager.getPassword() != null && manager.getPassword().equals(password)) {
            return manager;
        }
        return null;
    }
    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // Create a manager with a clear password, store hashed password
    public Manager createManager(String lastname, String firstname, String email, String clearPassword, String salt) {
        Manager manager = new Manager();
        manager.setLastname(lastname);
        manager.setFirstname(firstname);
        manager.setEmail(email);
        manager.setSalt(salt);
        manager.setPassword(passwordEncoder.encode(clearPassword));
        return managerRepository.save(manager);
    }

    public List<Manager> findAllFiltered(String lastname,
                                         String firstname,
                                         String email) {

        Specification<Manager> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (lastname != null && !lastname.isEmpty()) {
            spec = spec.and(ManagerSpecification.hasLastname(lastname));
        }

        if (firstname != null && !firstname.isEmpty()) {
            spec = spec.and(ManagerSpecification.hasFirstname(firstname));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and(ManagerSpecification.hasEmail(email));
        }

        return managerRepository.findAll(spec);
    }

}
