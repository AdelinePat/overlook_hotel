package overlook_hotel.overlook_hotel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import overlook_hotel.overlook_hotel.model.enumList.Job;

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
    private List<Integer> priceRange;
    private String password;
    private Job job;
}