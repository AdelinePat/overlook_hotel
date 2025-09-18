package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.specification.ClientSpecification;
import overlook_hotel.overlook_hotel.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAllFiltered(String lastname,
                                        String firstname,
                                        String email,
                                        String phone) {

//        Specification<Client> spec = Specification.where(null);

//        Specification<Client> spec = Specification.unrestricted();

        Specification<Client> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (lastname != null && !lastname.isBlank()) {
            spec = spec.and(ClientSpecification.hasLastname(lastname));
        }
        if (firstname != null && !firstname.isBlank()) {
            spec = spec.and(ClientSpecification.hasFirstname(firstname));
        }
        if (email != null && !email.isBlank()) {
            spec = spec.and(ClientSpecification.hasEmail(email));
        }
        if (phone != null && !phone.isBlank()) {
            spec = spec.and(ClientSpecification.hasPhone(phone));
        }

        return clientRepository.findAll(spec);
    }
    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }
}
