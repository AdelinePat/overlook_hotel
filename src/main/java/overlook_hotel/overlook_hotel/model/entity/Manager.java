package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "manager")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_manager")
    private Long id;

    @NotBlank
    @Column(name="lastname", length = 100)
    private String lastname;

    @NotBlank
    @Column(name="firstname", length = 100)
    private String firstname;

    @NotBlank
    @Column(name="email", length = 255)
    private String email;

    @NotBlank
    @Column(name="password", length = 255)
    private String password;

    @NotBlank
    @Column(name="salt", length = 255)
    private String salt;
}
