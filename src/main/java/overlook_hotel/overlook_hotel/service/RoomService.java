package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Feedback;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.RoomBonus;
import overlook_hotel.overlook_hotel.model.entity.Standing;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.repository.FeedbackRepository;
import overlook_hotel.overlook_hotel.repository.RoomBonusRepository;
import overlook_hotel.overlook_hotel.specification.RoomSpecification;
import overlook_hotel.overlook_hotel.repository.RoomRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final FeedbackRepository feedbackRepository;
    private final RoomBonusRepository roomBonusRepository;

    public RoomService(RoomRepository roomRepository, FeedbackRepository feedbackRepository, RoomBonusRepository roomBonusRepository) {
        this.roomRepository = roomRepository;
        this.feedbackRepository = feedbackRepository;
        this.roomBonusRepository = roomBonusRepository;
    }



    public List<Room> findAllFiltered(Integer number,
                                      Integer capacity,
                                      String description,
                                      Standing standing,
                                      BedType type,
                                      LocalDate startDate,
                                      LocalDate endDate,
                                      List<Integer> night_price,
                                      List<RoomBonusEnum> bonuses,
                                      List<Long> excludedRoomIds) {

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

        if (excludedRoomIds != null && !excludedRoomIds.isEmpty()) {
            spec = spec.and(RoomSpecification.idNotIn(excludedRoomIds));
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

    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }
    public List<Feedback> getRoomFeedback(Long roomId) {
        return feedbackRepository.findAllByRoomId(roomId);
    }

    public List<RoomBonus> getAllBonuses() {
        return roomBonusRepository.findAll();
    }

    public List<Room> findAllByIds(List<Long> ids) {
        Specification<Room> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        spec = spec.and(RoomSpecification.idIn(ids));
        return roomRepository.findAll(spec);
    }
}
