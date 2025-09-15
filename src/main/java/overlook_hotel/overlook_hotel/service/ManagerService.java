package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.entity.Manager;
import overlook_hotel.overlook_hotel.model.ManagerSpecification;
import overlook_hotel.overlook_hotel.repository.ManagerRepository;

import java.util.List;

@Service
public class ManagerService {
    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<Manager> findAllFiltered(String lastname,
                                         String firstname,
                                         String email) {
        Specification<Manager> spec = Specification.unrestricted();

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
