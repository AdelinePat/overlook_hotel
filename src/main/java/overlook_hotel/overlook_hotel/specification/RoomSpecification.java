package overlook_hotel.overlook_hotel.specification;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.RoomLinkReservation;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;
import overlook_hotel.overlook_hotel.model.entity.Standing;
import overlook_hotel.overlook_hotel.model.enumList.BedType;

import java.time.LocalDate;
import java.util.List;

public class RoomSpecification {
    public static Specification<Room> hasNumber(final Integer number) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .equal(root
                                .get("number"), number);
    }

    public static Specification<Room> hasCapacity(final Integer capacity) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .equal(root
                                .get("capacity"), capacity);
    }

    public static Specification<Room> hasStanding(final Standing standing) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .equal(root
                                .get("standing"), standing);
    }

    public static Specification<Room> hasBedType(final BedType type) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .equal(root
                                .get("type"), type);
    }
    public static Specification<Room> hasPriceLowerThanOrEqual(final Integer night_price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("nightPrice"), night_price);
    }

    public static Specification<Room> hasPriceGreaterThan(final Integer night_price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("nightPrice"), night_price);
    }

    public static Specification<Room> hasPriceBetween(final List<Integer> night_price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("nightPrice"), night_price.get(0), night_price.get(1));
    }

    public static Specification<Room> hasDescription(final String words) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("description")
                                .as(String.class), "%" + words + "%");
    }

//    public static Specification<Room> isAvailableBetween(LocalDate start, LocalDate end) {
//        return (root, query, criteriaBuilder) -> {
//            Join<Object, Object> linkJoin = root.join("roomReservationsList", JoinType.LEFT);
//            Join<Object, Object> reservationJoin = linkJoin.join("roomReservation", JoinType.LEFT);
//
//            Predicate overlap = criteriaBuilder.and(
//                    criteriaBuilder.lessThanOrEqualTo(reservationJoin.get("startDate"), end),
//                    criteriaBuilder.greaterThanOrEqualTo(reservationJoin.get("endDate"), start)
//            );
//
//            assert query != null;
//            query.distinct(true);
//            return criteriaBuilder.not(overlap);
//        };
//    }

    public static Specification<Room> isAvailableBetween(LocalDate start, LocalDate end) {
        return (root, query, criteriaBuilder) -> {

            Subquery<Long> sub = query.subquery(Long.class);

            Root<RoomLinkReservation> rlrRoot = sub.from(RoomLinkReservation.class);

            Join<RoomLinkReservation, RoomReservation> rrJoin = rlrRoot.join("roomReservation", JoinType.LEFT);

            sub.select(rlrRoot.get("id")).where(
                criteriaBuilder
                    .and(
                        criteriaBuilder.equal(rlrRoot.get("room").get("id"), root.get("id")),
                        criteriaBuilder.lessThanOrEqualTo(rrJoin.get("startDate"), end),
                        criteriaBuilder.greaterThanOrEqualTo(rrJoin.get("endDate"), start)
                    )
                );
            return criteriaBuilder.not(criteriaBuilder.exists(sub));
        };
    }


}
