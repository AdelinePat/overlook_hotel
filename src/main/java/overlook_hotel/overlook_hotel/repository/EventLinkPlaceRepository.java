package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import overlook_hotel.overlook_hotel.model.entity.EventLinkReservation;

public interface EventLinkPlaceRepository extends JpaRepository<EventLinkReservation, Long> {
}
