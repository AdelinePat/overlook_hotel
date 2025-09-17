package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;
import overlook_hotel.overlook_hotel.repository.RoomReservationRepository;
import overlook_hotel.overlook_hotel.specification.RoomReservationSpecification;
import overlook_hotel.overlook_hotel.specification.RoomSpecification;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RoomReservationService {
    private final RoomReservationRepository roomReservationRepository;

    public RoomReservationService(final RoomReservationRepository roomReservationRepository) {
        this.roomReservationRepository = roomReservationRepository;
    }

    public List<RoomReservation> findAllFiltered(Integer idReservation,
                                                 String lastname,
                                                 String firstname) {
        Specification<RoomReservation> spec =
                (root, query, criteriaBuilder)
                        -> criteriaBuilder.conjunction();

        if (lastname != null && !lastname.isBlank()) {
            spec = spec.and(RoomReservationSpecification.hasClientLastname(lastname));
        }

        if (firstname != null && !firstname.isBlank()) {
            spec = spec.and(RoomReservationSpecification.hasClientFirstname(firstname));
        }

        if (idReservation != null) {
            spec = spec.and(RoomReservationSpecification.hasIdReservation(idReservation));
        }

        return roomReservationRepository.findAll(spec);
    }
}
