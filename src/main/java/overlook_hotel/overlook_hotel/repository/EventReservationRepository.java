package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import overlook_hotel.overlook_hotel.model.entity.EventReservation;
import java.util.List;

public interface EventReservationRepository extends JpaRepository<EventReservation, Long> {
    List<EventReservation> findByClient_Id(Long clientId);

    List<EventReservation> findByClientId(Long id);

    List<EventReservation> findByClientIdOrderByStartDateAsc(Long id);

    List<EventReservation> findByClientIdOrderByStartDateDesc(Long id);
}
