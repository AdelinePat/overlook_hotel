package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import overlook_hotel.overlook_hotel.entity.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer>, JpaSpecificationExecutor<Manager> {
}
