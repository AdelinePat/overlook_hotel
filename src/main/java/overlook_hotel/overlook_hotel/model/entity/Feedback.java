package overlook_hotel.overlook_hotel.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "feedback")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_room_reservation", referencedColumnName = "id_room_reservation")
    private RoomReservation roomReservation;

    @NotNull
    @Column(name="rate")
    private Integer rate;

    @NotBlank
    @Column(name="comment")
    private String comment;
}
