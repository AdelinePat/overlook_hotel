package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomReservationRepository  extends JpaRepository<RoomReservation, Long>, JpaSpecificationExecutor<RoomReservation> {

    // 1) Fetch reservation with links + rooms to avoid N+1 (entity approach)
    @Query("SELECT rr FROM RoomReservation rr " +
            "LEFT JOIN FETCH rr.roomLinks rl " +
            "LEFT JOIN FETCH rl.room r " +
            "WHERE rr.id = :id")
    Optional<RoomReservation> findByIdWithLinks(@Param("id") Long id);

    // 2) Native query to compute total price directly in DB (fast, single query)
    @Query(value = "SELECT (SUM(r.night_price) * DATEDIFF(rr.end_date, rr.start_date)) " +
            "FROM room_reservation rr " +
            "JOIN room_link_reservation link ON rr.id_room_reservation = link.id_room_reservation " +
            "JOIN room r ON link.id_room = r.id_room " +
            "WHERE rr.id_room_reservation = :id " +
            "GROUP BY rr.id_room_reservation, rr.start_date, rr.end_date",
            nativeQuery = true)
    BigDecimal findTotalPriceByReservationIdNative(@Param("id") Long reservationId);

    // 3) Native query returning cart items (room level) — will map to Object[] and be converted in service
    @Query(value =
            "SELECT r.id_room, r.number, r.night_price, COUNT(*) AS quantity, " +
                    "(COUNT(*) * r.night_price * DATEDIFF(rr.end_date, rr.start_date)) AS total_cost " +
                    "FROM room_reservation rr " +
                    "JOIN room_link_reservation link ON rr.id_room_reservation = link.id_room_reservation " +
                    "JOIN room r ON link.id_room = r.id_room " +
                    "WHERE rr.id_room_reservation = :id " +
                    "GROUP BY r.id_room, r.number, r.night_price",
            nativeQuery = true)
    List<Object[]> findCartByReservationIdNative(@Param("id") Long reservationId);
}
