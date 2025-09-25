package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import overlook_hotel.overlook_hotel.model.entity.EventReservation;

public interface EventReservationRepository extends JpaRepository <EventReservation, Long>{
}
