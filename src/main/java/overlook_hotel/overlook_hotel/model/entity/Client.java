package overlook_hotel.overlook_hotel.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @Column(name = "id_client", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "lastname", length = 100)
    private String lastname;

    @NotBlank
    @Column(name = "firstname", length = 100)
    private String firstname;

    @NotBlank
    @Column(name = "email", length = 255)
    private String email;

    @NotBlank
    @Column(name = "phone", length = 15)
    private String phone;

    @NotBlank
    @Column(name = "password", length = 255)
    private String password;

    @NotBlank
    @Column(name = "salt", length = 255)
    private String salt;
}
