package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "standing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Standing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_standing")
    private Long id;

    @Column(name = "name")
    private String name;
}