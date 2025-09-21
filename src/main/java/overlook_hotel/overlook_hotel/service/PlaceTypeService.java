package overlook_hotel.overlook_hotel.service;

import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.PlaceType;
import overlook_hotel.overlook_hotel.repository.PlaceTypeRepository;

import java.util.List;

@Service
public class PlaceTypeService {
    private final PlaceTypeRepository placeTypeRepository;

    public PlaceTypeService(PlaceTypeRepository placeTypeRepository) {
        this.placeTypeRepository = placeTypeRepository;
    }

    public PlaceType findById(Long id) {
        return placeTypeRepository.findById(id).orElse(null);
    }

    public List<PlaceType> getAll() {
        return placeTypeRepository.findAll();
    }
}
