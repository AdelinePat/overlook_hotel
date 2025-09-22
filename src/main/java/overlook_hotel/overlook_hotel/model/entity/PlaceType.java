package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;


@Entity
@Table(name ="place_type")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceType {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_place_type")
    private Long id;


    @NotNull
    @Column(name = "name", length = 100)
    private String name;

}
