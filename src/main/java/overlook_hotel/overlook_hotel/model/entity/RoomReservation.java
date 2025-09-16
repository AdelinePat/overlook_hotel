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

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    private Client client;

    @NotNull
    @Column(name="creation_date")
    private LocalDate creation_date;

    @NotNull
    @Column(name="start_date")
    private LocalDate start_date;

    @NotNull
    @Column(name="end_date")
    private LocalDate end_date;

    @NotNull
    @Column(name="status")
    private Boolean status;

    @Column(name="payment_date")
    private LocalDateTime payment_date;

    // TODO PROBABLY DELETE THIS FIELD ?
    @Column(name="total_price")
    private BigDecimal total_price;
}
