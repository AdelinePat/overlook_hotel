package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import overlook_hotel.overlook_hotel.model.entity.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query("""
    SELECT f FROM Feedback f
    JOIN f.roomReservation rr
    JOIN rr.roomLinks link
    JOIN link.room r
    WHERE r.id = :roomId
    """)
    public List<Feedback> findAllByRoomId(@Param("roomId") Long roomId);
}
