package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_reservation")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_reservation")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    private Client client;

    @NotNull
    @Column(name="creation")
    private LocalDate creationDate;

    @NotNull
    @Column(name="start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name="end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name="status")
    private Boolean status;

    @Column(name="payment_date")
    private LocalDateTime paymentDate;

    // TODO PROBABLY DELETE THIS FIELD ?
//    @Column(name="total_price")
    private BigDecimal totalPrice;
}
