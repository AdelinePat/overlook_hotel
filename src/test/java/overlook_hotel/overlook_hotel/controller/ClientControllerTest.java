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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
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


         when(mockClientService.findAllFiltered(any(), any(), any(), any()))
                 .thenReturn(clientsList); // works

//        when(mockClientService.findAllFiltered(any(), any(), any(), any()))
//            .thenAnswer(invocation -> {
//                System.out.println("findAllFiltered (any()) called with: " +
//                invocation.getArgument(0) + ", " +
//                invocation.getArgument(1) + ", " +
//                invocation.getArgument(2) + ", " +
//                invocation.getArgument(3));
//            return clientsList;
//        });

        String clientPage = clientController.clients(
            "",
            "",
            "",
            "",
            "",
            "",
            null, 
            false,
            "",
            model);  

        List<Client> clients = (List<Client>) model.getAttribute("rows");

        assertEquals("table", clientPage); //ok
        assertNotNull(clients, "clients list should not be null"); //ok
        // assertEquals(3, clientsList.size()); // ok
        assertEquals(3, clients.size());


        System.out.println("\n\n\n\t\t\t ############## Liste des clients retournée par le controller :");
        for (Client client : clients) {
            System.out.println(client.getLastname() +
                " " + client.getFirstname() +
                " " + client.getEmail() +
                " " + client.getPhone());
        }
        System.out.println("\t\t\t ############## TestClientController fin\n\n\n");
    }
}
