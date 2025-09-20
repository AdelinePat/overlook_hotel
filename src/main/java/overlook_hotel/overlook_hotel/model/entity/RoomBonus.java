package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "room_bonus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomBonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_bonus")
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private RoomBonusEnum type;

    @NotNull
    @Column(name="daily_price")
    private BigDecimal daily_price;

    @ManyToMany(mappedBy = "bonuses")
    private List<Room> rooms;

}
