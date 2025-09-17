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
    private final ClientController clientController = new ClientController();

    @Test
    public void testClientController() {
        Model model = new ConcurrentModel();
        System.out.println("\n\n\n\t\t\t ############## TestClientController début");

        String index = clientController.index(model);

        assertEquals("index", index);

        System.out.println("\t\t\t ############## TestClientController fin\n\n\n");
    }
}
