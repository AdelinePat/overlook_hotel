package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overlook_hotel.overlook_hotel.model.entity.Standing;

import java.util.Optional;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long> {
    Optional<Standing> findByName(String name);
}