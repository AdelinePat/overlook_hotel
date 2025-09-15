package overlook_hotel.overlook_hotel.model;
import org.springframework.data.jpa.domain.Specification;
import overlook_hotel.overlook_hotel.entity.Employee;

public class EmployeeSpecification {
    public static Specification<Employee> hasLastname(final String lastname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("lastname")
                                .as(String.class), "%" + lastname + "%");
    }

    public static Specification<Employee> hasFirstname(final String firstname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("firstname")
                                .as(String.class), "%" + firstname + "%");
    }

    public static Specification<Employee> hasEmail(final String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(root
                                .get("email")
                                .as(String.class), "%" + email + "%");
    }

    public static Specification<Employee> hasJob(final Job job) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .equal(root
                                .get("job"),
                                job);
    }
}
