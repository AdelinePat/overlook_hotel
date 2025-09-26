package overlook_hotel.overlook_hotel.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import overlook_hotel.overlook_hotel.model.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
	Client findByEmail(String email);
}