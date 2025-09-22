package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_link_place")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EventLinkReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event_link_place")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_event_reservation", nullable = false)
    private EventReservation eventReservation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_place")
    private Place place;
}
