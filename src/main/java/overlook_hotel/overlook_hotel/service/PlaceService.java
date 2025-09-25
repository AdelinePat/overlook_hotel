package overlook_hotel.overlook_hotel.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Place;
import overlook_hotel.overlook_hotel.model.entity.PlaceType;
import overlook_hotel.overlook_hotel.repository.PlaceRepository;
import overlook_hotel.overlook_hotel.specification.PlaceSpecification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }
    public List<Place> findAllFiltered(String placeTypeName,
                                       Integer minCapacity,
                                       LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       BigDecimal minPrice,
                                       BigDecimal maxPrice) {

        Specification<Place> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (placeTypeName != null && !placeTypeName.isBlank()) {
            spec = spec.and(PlaceSpecification.hasPlaceTypeName(placeTypeName));
        }

        if (minCapacity != null) {
            spec = spec.and(PlaceSpecification.hasCapacityAtLeast(minCapacity));
        }

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(PlaceSpecification.priceBetween(minPrice, maxPrice));
        } else if (minPrice != null) {
            spec = spec.and(PlaceSpecification.priceGte(minPrice));
        } else if (maxPrice != null) {
            spec = spec.and(PlaceSpecification.priceLte(maxPrice));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(PlaceSpecification.isAvailableBetween(startDate, endDate));
        }

        return placeRepository.findAll(spec);

    }

    public Place findById(Long id) {

        return placeRepository.findById(id).orElse((null));

    }


}
