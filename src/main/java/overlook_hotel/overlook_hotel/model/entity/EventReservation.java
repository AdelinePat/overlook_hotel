package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
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
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @NotNull
    @Column(name="end_date")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;

    @Column(name="used_fidelity")
    private Integer usedFidelity;

    @OneToMany(mappedBy = "eventReservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EventLinkReservation> placeLinks = new ArrayList<>();

    @NotNull
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

}
