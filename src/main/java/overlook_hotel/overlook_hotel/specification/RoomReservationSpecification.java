package overlook_hotel.overlook_hotel.specification;
import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;

import java.time.LocalDate;

public class RoomReservationSpecification {
    public static Specification<RoomReservation> byClientId(Long clientId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("client").get("id"), clientId);
    }

    public static Specification<RoomReservation> isConfirmed() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), true);
    }

    public static Specification<RoomReservation> isNotConfirmed() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), false);
    }

    public static Specification<RoomReservation> betweenDates(LocalDate start, LocalDate end) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), start),
                criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), end)
        );
    }
}
