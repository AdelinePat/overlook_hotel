package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.Standing;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.specification.RoomSpecification;
import overlook_hotel.overlook_hotel.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAllFiltered(Integer number,
                                      Integer capacity,
                                      String description,
                                      Standing standing,
                                      BedType type,
                                      LocalDate startDate,
                                      LocalDate endDate,
                                      List<Integer> night_price,
                                      List<RoomBonusEnum> bonuses) {

        Specification<Room> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (description != null && !description.isBlank()) {
            spec = spec.and(RoomSpecification.hasDescription(description));
        }

        if (number != null) {
            spec = spec.and(RoomSpecification.hasNumber(number));
        }

        if (capacity != null) {
            spec = spec.and(RoomSpecification.hasCapacity(capacity));
        }

        if (standing != null) {
            spec = spec.and(RoomSpecification.hasStanding(standing));
        }

        if (standing != null) {
            spec = spec.and(RoomSpecification.hasStanding(standing));
        }
        if (type != null) {
            spec = spec.and(RoomSpecification.hasBedType(type));
        }

        if (bonuses != null && !bonuses.isEmpty()) {
            Specification<Room> bonusSpec = null;
            for (RoomBonusEnum bonus : bonuses) {
                Specification<Room> singleBonusSpec = RoomSpecification.hasBonus(bonus);
                bonusSpec = (bonusSpec == null) ? singleBonusSpec : bonusSpec.and(singleBonusSpec);
            }
            spec = spec.and(bonusSpec);
        }

//        TODO NIGHT_PRICE ONLY LOWER THAN !!!!!!
        if (night_price != null) {
            if ((night_price.get(0) != null || night_price.get(1) != null)) {
                spec = this.filteredByPrice(spec, night_price);
            }
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(RoomSpecification.isAvailableBetween(startDate, endDate));
        }

        return roomRepository.findAll(spec);
    }

    private Specification<Room> filteredByPrice(Specification<Room> spec, List<Integer> priceRange) {
        if (priceRange.get(1) == null) {
            return spec.and(RoomSpecification.hasTotalPriceGreaterThan(priceRange.get(0)));
        } else if (priceRange.get(0) == null) {
            return spec.and(RoomSpecification.hasTotalPriceLowerThanOrEqual(priceRange.get(1)));
        } else {
            return spec.and(RoomSpecification.hasTotalPriceBetween(priceRange));
        }
    }
}
