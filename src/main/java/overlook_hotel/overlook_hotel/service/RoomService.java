package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.specification.RoomSpecification;
import overlook_hotel.overlook_hotel.model.enumList.RoomStanding;
import overlook_hotel.overlook_hotel.repository.RoomRepository;

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
                                      RoomStanding standing,
                                      BedType type,
                                      List<Integer> night_price) {

        Specification<Room> spec = Specification.unrestricted();

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

//        TODO NIGHT_PRICE ONLY LOWER THAN !!!!!!
        if (night_price != null) {
            spec = this.filteredByPrice(spec, night_price);
        }

        return roomRepository.findAll(spec);
    }

    private Specification<Room> filteredByPrice(Specification<Room> spec, List<Integer> priceRange) {
        if (priceRange.get(1) == null) {
            return spec.and(RoomSpecification.hasPriceLowerThanOrEqual(priceRange.get(0)));
        } else if (priceRange.get(0) == null) {
            return spec.and(RoomSpecification.hasPriceGreaterThan(priceRange.get(1)));
        } else {
            return spec.and(RoomSpecification.hasPriceBetween(priceRange));
        }
    }
}
