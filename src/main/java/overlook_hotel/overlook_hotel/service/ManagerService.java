package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.Manager;
import overlook_hotel.overlook_hotel.specification.ManagerSpecification;
import overlook_hotel.overlook_hotel.repository.ManagerRepository;

import java.util.List;

@Service
public class ManagerService {
    public Manager authenticate(String email, String password) {
        Manager manager = managerRepository.findByEmail(email);
        if (manager != null && manager.getPassword() != null && manager.getPassword().equals(password)) {
            return manager;
        }
        return null;
    }
    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
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
