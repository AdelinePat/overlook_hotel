package overlook_hotel.overlook_hotel.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationHistoryDTO {
    private Long id;
    private LocalDate creationDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfNights;
    private int numberOfRooms;
    private LocalDateTime paymentDate;
    private BigDecimal totalPrice;
    private List<RoomReservationFields> rooms = new ArrayList<>();
}
