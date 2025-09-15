package overlook_hotel.overlook_hotel.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import overlook_hotel.overlook_hotel.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {
}