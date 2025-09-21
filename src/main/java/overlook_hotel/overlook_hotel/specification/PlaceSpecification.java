package overlook_hotel.overlook_hotel.specification;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.model.entity.EventLinkReservation;
import overlook_hotel.overlook_hotel.model.entity.Place;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class PlaceSpecification {
    private PlaceSpecification() {}


    public static Specification<Place> hasPlaceTypeName(String placeTypeName) {
        return (root, query, cb) -> {
            if (placeTypeName == null || placeTypeName.isBlank()) {
                return cb.conjunction();
            }
            var typeJoin = root.join("placeType", JoinType.LEFT);
            return cb.like(cb.lower(typeJoin.get("name")), "%" + placeTypeName.toLowerCase() + "%");
        };
    }

    public static Specification<Place> hasCapacityAtLeast(Integer minCapacity) {
        return (root, query, cb) -> minCapacity == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("capacity"), minCapacity);
    }

    // Prix
    public static Specification<Place> priceGte(BigDecimal minPrice) {
        return (root, query, cb) -> minPrice == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("hourlyPrice"), minPrice);
    }

    public static Specification<Place> priceLte(BigDecimal maxPrice) {
        return (root, query, cb) -> maxPrice == null
                ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("hourlyPrice"), maxPrice);
    }

    public static Specification<Place> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return priceGte(minPrice).and(priceLte(maxPrice));
    }

    // Disponibilité
    public static Specification<Place> isAvailableBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null || end == null) {
                return cb.conjunction();
            }

            Subquery<Long> sub = query.subquery(Long.class);
            var elr = sub.from(EventLinkReservation.class);
            var er  = elr.join("eventReservation", JoinType.INNER);

            var samePlace = cb.equal(elr.get("place"), root);
            var overlap = cb.and(
                    cb.lessThan(er.get("startDate"), end),
                    cb.greaterThan(er.get("endDate"), start)
            );

            sub.select(cb.literal(1L)).where(cb.and(samePlace, overlap));

            return cb.not(cb.exists(sub));
        };
    }
}
