package overlook_hotel.overlook_hotel.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.service.ClientService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Disabled
public class ClientControllerTest {

    private final ClientService mockClientService = mock(ClientService.class);
    private final ClientController clientController = new ClientController(mockClientService);

    @Test
    public void testClientController() {
            Model model = new ConcurrentModel();
            System.out.println("\n\n\n\t\t\t ############## TestClientService début");

            List<Client> clientsList = Arrays.asList(
                    new Client(1L, "John", "Doe", "johndoe@gmail.com", "0001020304", "pass","salt1"),
                    new Client(2L, "flo", "rence", "florence@gmail.com", "0102030405", "ssap","salt2"),
                    new Client(3L, "thibault", "noname", "thibaultnoname@gmail.com", "0203040506", "password","salt3"));

            when(mockClientService.findAllFiltered(eq(""), eq(""), eq(""), eq("")))
                    .thenReturn(clientsList);


        String clientPage = clientController.clients(
                "",
                "",
                "",
                "",
                null,
                false,
                null,
                model);

        List<Client> clients = (List<Client>) model.getAttribute("rows");

        for( Client client : clients ) {
            System.out.println(client.getLastname() +
                    " " + client.getFirstname() +
                    " " + client.getEmail() +
                    " " + client.getPhone());
        }

        assertEquals("table", clientPage);
        assertEquals(3, clients.size());

        System.out.println("\t\t\t ############## TestClientController fin\n\n\n");
    }
}
