package overlook_hotel.overlook_hotel.specification;
import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;

public class RoomReservationSpecification {
    public static Specification<RoomReservation> hasIdReservation(final Integer idReservation) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), idReservation);
    }

    public static Specification<RoomReservation> hasClientLastname(final String lastname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root
                        .get("client")
                        .get("lastname"),
                        "%" + lastname + "%");
    }

    public static Specification<RoomReservation> hasClientFirstname(final String firstname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root
                                .get("client")
                                .get("firstname"),
                        "%" + firstname + "%");
    }
}
