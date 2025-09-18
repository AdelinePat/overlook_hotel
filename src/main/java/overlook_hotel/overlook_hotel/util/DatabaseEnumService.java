package overlook_hotel.overlook_hotel.util;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
public class DatabaseEnumService {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Returns the list of enum values for a given table and field.
     */
    public List<String> listEnums(String table, String field) {
        String sql = String.format("SHOW COLUMNS FROM %s WHERE Field = '%s'", table, field);
        List<Object[]> result = entityManager.createNativeQuery(sql).getResultList();
        if (result == null || result.isEmpty() || result.get(0).length < 2) return List.of();
        String type = result.get(0)[1].toString();
        return DatabaseEnumUtils.parseEnumValues(type);
    }
}
