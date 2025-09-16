package overlook_hotel.overlook_hotel.service;
import org.junit.jupiter.api.Disabled;
import overlook_hotel.overlook_hotel.model.entity.Client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
@Disabled
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void findAllClients() {
        List<Client> clients = clientService.findAllFiltered("", "", "", "");
        System.out.println("\t\t #### 1 #### liste des clients:");
        for (Client client : clients) {
            System.out.println("\t" + client.getLastname() + " " + client.getFirstname());
        }
        assertEquals(20, clients.size());
    }

    @Test
    public void findClientByLastname() {
        List<Client> clients = clientService.findAllFiltered("M", "", "", "");
        System.out.println("\t\t #### 2 #### liste des clients nom:");
        for (Client client : clients) {
            System.out.println("\t" + client.getLastname() + " " + client.getFirstname());
        }
        assertEquals(4, clients.size());
    }

    @Test
    public void findClientByLastnameAndFirstname() {
        List<Client> clients = clientService.findAllFiltered("M", "o", "", "");
        System.out.println("\t\t #### 3 #### liste des clients nom prénom:");
        for (Client client : clients) {
            System.out.println("\t" + client.getLastname() + " " + client.getFirstname());
        }
        assertEquals(2, clients.size());
    }
}
