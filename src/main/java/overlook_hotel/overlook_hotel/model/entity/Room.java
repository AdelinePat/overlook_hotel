package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomStanding;

import java.math.BigDecimal;

@Entity
@Table(name = "room")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private int id;

    @NotNull
    @Column(name="number")
    private int number;

    @NotNull
    @Column(name="capacity")
    private int capacity;

    @NotBlank
    @Column(name="description", length = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="standing", length = 15)
    private RoomStanding standing;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="type", length = 15)
    private BedType type;

    @NotNull
    @Column(name="night_price")
    private BigDecimal night_price;
}
