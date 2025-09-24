package overlook_hotel.overlook_hotel.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room_reservation_bonus")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomReservationBonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_reservation_bonus")
    private Long id;

    // The room in this reservation that gets the bonus
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room")
    private Room room;

    // The reservation this bonus belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room_reservation")
    private RoomReservation roomReservation;

    // The bonus itself
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_room_bonus")
    private RoomBonus bonus;
}
