package overlook_hotel.overlook_hotel.specification;

import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.entity.Client;

public class ClientSpecification {
    public static Specification<Client> hasLastname(final String lastname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("lastname")
                                .as(String.class), "%" + lastname + "%");
    }

    public static Specification<Client> hasFirstname(final String firstname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("firstname")
                                .as(String.class), "%" + firstname + "%");
    }

    public static Specification<Client> hasEmail(final String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("email")
                                .as(String.class), "%" + email + "%");
    }

    public static Specification<Client> hasPhone(final String phone) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .equal(root
                                .get("phone")
                                .as(String.class), "%" + phone + "%");
    }
}
