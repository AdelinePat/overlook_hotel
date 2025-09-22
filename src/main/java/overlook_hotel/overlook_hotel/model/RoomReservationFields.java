package overlook_hotel.overlook_hotel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import overlook_hotel.overlook_hotel.model.entity.Job;
import overlook_hotel.overlook_hotel.model.entity.Standing;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationFields {
    private Long id;
    private List<Integer> priceRange = new ArrayList<>(Arrays.asList(null, null));
    private Integer roomNumber;
    private Integer capacity;
    private String description;
    private Standing standing;
    private LocalDate startDate;
    private LocalDate endDate;
    private BedType bedType;
    List<RoomBonusEnum> bonuses;
    List<RoomBonusEnum> additionalBonuses;
}