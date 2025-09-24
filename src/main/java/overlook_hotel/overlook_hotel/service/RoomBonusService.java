package overlook_hotel.overlook_hotel.service;

import lombok.RequiredArgsConstructor;
import overlook_hotel.overlook_hotel.model.entity.RoomBonus;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;
import overlook_hotel.overlook_hotel.repository.RoomBonusRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomBonusService {

    private final RoomBonusRepository bonusRepository;

    public RoomBonus findBonusByType(RoomBonusEnum type) {
        return bonusRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("Bonus not found for type: " + type));
    }
}
