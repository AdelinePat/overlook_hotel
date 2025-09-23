package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import overlook_hotel.overlook_hotel.model.entity.RoomBonus;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;

import java.util.Optional;

public interface RoomBonusRepository extends JpaRepository<RoomBonus, Long> {
    Optional<RoomBonus> findByType(RoomBonusEnum type);
}
