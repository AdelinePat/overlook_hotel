package overlook_hotel.overlook_hotel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import overlook_hotel.overlook_hotel.model.enumList.Job;

@Entity
@Table(name = "employee")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employee")
    private int id;

    @NotBlank
    @Column(name="lastname", length = 100)
    private String lastname;

    @NotBlank
    @Column(name="firstname", length = 100)
    private String firstname;

    @NotBlank
    @Column(name="email", length = 255)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="job", length = 20)
    private Job job;

    @NotBlank
    @Column(name="password", length = 255)
    private String password;

    @NotBlank
    @Column(name="salt", length = 255)
    private String salt;

}
