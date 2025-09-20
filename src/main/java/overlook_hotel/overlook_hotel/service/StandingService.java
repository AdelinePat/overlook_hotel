package overlook_hotel.overlook_hotel.service;

import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Standing;
import overlook_hotel.overlook_hotel.repository.StandingRepository;

import java.util.List;

@Service
public class StandingService {
    private final StandingRepository standingRepository;

    public StandingService(StandingRepository standingRepository) {
        this.standingRepository = standingRepository;
    }

    public Standing findStandingById(Long id) {
        return standingRepository.findById(id).orElse(null);
    }

    public List<Standing> getFullStandingList() {
        return standingRepository.findAll();
    }

}
