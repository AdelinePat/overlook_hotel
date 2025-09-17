package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;

@Repository
public interface RoomReservationRepository  extends JpaRepository<RoomReservation, Integer>, JpaSpecificationExecutor<RoomReservation> {
}
