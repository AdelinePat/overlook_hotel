package overlook_hotel.overlook_hotel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientFilter {
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
}