package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overlook_hotel.overlook_hotel.model.entity.RoomLinkReservation;

import java.util.Optional;

@Repository
public interface RoomLinkReservationRepository extends JpaRepository<RoomLinkReservation, Long> {
}
