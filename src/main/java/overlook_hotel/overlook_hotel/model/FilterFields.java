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
public class FilterFields {
    private Long id;
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private List<Integer> priceRange = new ArrayList<>(Arrays.asList(null, null));
    private String password;
    private Job job;
    private Integer roomNumber;
    private Integer capacity;
    private String description;
    private Standing standing;
    private LocalDate startDate;
    private LocalDate endDate;
    private BedType bedType;
    private RoomBonusEnum bonus;
}