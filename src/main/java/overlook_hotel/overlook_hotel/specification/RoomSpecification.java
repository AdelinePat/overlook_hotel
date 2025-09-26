package overlook_hotel.overlook_hotel.specification;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.model.entity.*;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;

import java.math.BigDecimal;
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
                criteriaBuilder.greaterThanOrEqualTo(root.get("nightPrice"), night_price);
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

    public static Specification<Room> hasBonus(RoomBonusEnum bonus) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Room, RoomBonus> bonusJoin = root.join("bonuses", JoinType.INNER);
            return criteriaBuilder.equal(bonusJoin.get("type"), bonus.name());
        };
    }
    public static Specification<Room> hasTotalPriceLowerThanOrEqual(final Integer night_price) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Join<Room, RoomBonus> bonusJoin = root.join("bonuses", JoinType.LEFT);
            Expression<BigDecimal> totalBonus = criteriaBuilder
                    .sum(
                            criteriaBuilder.coalesce(
                                    bonusJoin.get("dailyPrice"), BigDecimal.ZERO)
                    );

            Expression<BigDecimal> totalPrice = criteriaBuilder
                    .sum(
                    root.get("nightPrice"), totalBonus);

            query.groupBy(root.get("id"));

            query.having(
                    criteriaBuilder
                            .lessThanOrEqualTo(totalPrice, BigDecimal.valueOf(night_price)));
            return criteriaBuilder.conjunction();

        };  
    }

    public static Specification<Room> hasTotalPriceGreaterThan(final Integer night_price) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Room, RoomBonus> bonusJoin = root.join("bonuses", JoinType.LEFT);
            Expression<BigDecimal> totalBonus = criteriaBuilder
                    .sum(
                            criteriaBuilder.coalesce(
                                    bonusJoin.get("dailyPrice"), BigDecimal.ZERO)
                    );

            Expression<BigDecimal> totalPrice = criteriaBuilder
                    .sum(
                            root.get("nightPrice"), totalBonus);

            query.groupBy(root.get("id"));

            query.having(criteriaBuilder.greaterThanOrEqualTo(totalPrice, BigDecimal.valueOf(night_price)));



            return criteriaBuilder.conjunction();

        };
    }

    public static Specification<Room> hasTotalPriceBetween(final List<Integer> night_price) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Join<Room, RoomBonus> bonusJoin = root.join("bonuses", JoinType.LEFT);
            Expression<BigDecimal> totalBonus = criteriaBuilder
                    .sum(
                            criteriaBuilder.coalesce(
                                    bonusJoin.get("dailyPrice"), BigDecimal.ZERO)
                    );

            Expression<BigDecimal> totalPrice = criteriaBuilder
                    .sum(
                            root.get("nightPrice"), totalBonus);

            query.groupBy(root.get("id"));

            query.having(criteriaBuilder
                    .between(totalPrice,
                            BigDecimal.valueOf(night_price.get(0)),
                            BigDecimal.valueOf(night_price.get(1))
                    ));

            return criteriaBuilder.conjunction();

        };
    }

    public static Specification<Room> idNotIn(List<Long> excludedIds) {
        return (root, query, criteriaBuilder) -> {
                criteriaBuilder.conjunction(); // no filtering
            return root.get("id").in(excludedIds).not();
        };
    }

    public static Specification<Room> idIn(List<Long> ids) {
        return (root, query, criteriaBuilder) -> root.get("id").in(ids);
    }



}
