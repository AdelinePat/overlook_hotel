package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "place")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_place")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_place_type")
    private PlaceType placeType;

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull
    @Column(name = "hourly_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal hourlyPrice;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<EventLinkReservation> eventLinks;
}
