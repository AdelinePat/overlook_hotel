package overlook_hotel.overlook_hotel.model;
import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.entity.Manager;

public class ManagerSpecification {
    public static Specification<Manager> hasLastname(final String lastname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("lastname")
                                .as(String.class), "%" + lastname + "%");
    }

    public static Specification<Manager> hasFirstname(final String firstname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("firstname")
                                .as(String.class), "%" + firstname + "%");
    }

    public static Specification<Manager> hasEmail(final String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("email")
                                .as(String.class), "%" + email + "%");
    }
}
