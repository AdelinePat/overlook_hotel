package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room_link_reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomLinkReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_link_reservation")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_room_reservation")
    private RoomReservation roomReservation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_room")
    private Room room;
}
