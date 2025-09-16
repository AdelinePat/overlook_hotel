package overlook_hotel.overlook_hotel.specification;
import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomStanding;

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

    public static Specification<Room> hasStanding(final RoomStanding standing) {
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
                criteriaBuilder.lessThanOrEqualTo(root.get("night_price"), night_price);
    }

    public static Specification<Room> hasPriceGreaterThan(final Integer night_price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("night_price"), night_price);
    }

    public static Specification<Room> hasPriceBetween(final List<Integer> night_price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("night_price"), night_price.get(0), night_price.get(1));
    }

    public static Specification<Room> hasDescription(final String words) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("description")
                                .as(String.class), "%" + words + "%");
    }


}
