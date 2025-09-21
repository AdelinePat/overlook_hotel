package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import overlook_hotel.overlook_hotel.model.enumList.EventType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_reservation")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event_reservation")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    private Client client;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event", length = 100, nullable = false)
    private EventType eventType;


    @NotNull
    @Column(name="start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name="end_date")
    private LocalDate endDate;


    @OneToMany(mappedBy = "eventReservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EventLinkReservation> placeLinks = new ArrayList<>();

    @Transient
    private BigDecimal totalPrice;

}
