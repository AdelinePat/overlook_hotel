package overlook_hotel.overlook_hotel.repository;

import overlook_hotel.overlook_hotel.model.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlaceRepository
        extends JpaRepository<Place, Long>, JpaSpecificationExecutor<Place> {
}
