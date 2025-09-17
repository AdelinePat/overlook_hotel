package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;
import overlook_hotel.overlook_hotel.repository.RoomReservationRepository;
import overlook_hotel.overlook_hotel.specification.RoomReservationSpecification;
import overlook_hotel.overlook_hotel.specification.RoomSpecification;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RoomReservationService {
    private final RoomReservationRepository roomReservationRepository;

    public RoomReservationService(final RoomReservationRepository roomReservationRepository) {
        this.roomReservationRepository = roomReservationRepository;
    }

    public List<RoomReservation> findAllFiltered(Long idReservation,
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

        List<RoomReservation> reservations = roomReservationRepository.findAll(spec);
        reservations.forEach(this::calculateTotalPrice);

        return reservations;
    }

    private void calculateTotalPrice(RoomReservation reservation) {
        if (reservation.getRoomLinks() == null || reservation.getRoomLinks().isEmpty()) {
            reservation.setTotalPrice(BigDecimal.ZERO);
            return;
        }

        int nights = (int) ChronoUnit.DAYS.between(
                reservation.getStartDate(),
                reservation.getEndDate()
        );

        BigDecimal sumNightPrices = reservation.getRoomLinks().stream()
                .map(link -> link.getRoom().getNightPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        reservation.setTotalPrice(sumNightPrices.multiply(BigDecimal.valueOf(nights)));
    }


}
