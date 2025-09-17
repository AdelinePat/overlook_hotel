package overlook_hotel.overlook_hotel.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import overlook_hotel.overlook_hotel.model.entity.RoomReservation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

@SpringBootTest
public class RoomReservationServiceTest {

    @Autowired
    private RoomReservationService roomReservationService;

    @Test
    public void findAll() {
        List<RoomReservation> reservations = roomReservationService.findAllFiltered(null, "", "");
        System.out.println("\n\n\t\t #### 1 #### liste des room reservations");
        for (RoomReservation reservation : reservations) {
            System.out.println("\t" + reservation.getId()+
                    " " + reservation.getClient().getFirstname() +
                    " " + reservation.getClient().getLastname() +
                    " " + reservation.getPaymentDate() +
                    " " + reservation.getTotalPrice());
        }

        assertEquals(10, reservations.size());
    }

    @Test
    public void findByReservationID() {
        List<RoomReservation> reservations = roomReservationService.findAllFiltered(5L, "", "");
        System.out.println("\n\n\t\t #### 2 #### liste des room reservations par ID");
        for (RoomReservation reservation : reservations) {
            System.out.println("\t" + reservation.getId()+
                    " " + reservation.getClient().getFirstname() +
                    " " + reservation.getClient().getLastname() +
                    " " + reservation.getPaymentDate() +
                    " " + reservation.getTotalPrice());
        }

        assertEquals(1, reservations.size());
    }

    @Test
    public void findByClientLastname() {
        List<RoomReservation> reservations = roomReservationService.findAllFiltered(null, "au", "");
        System.out.println("\n\n\t\t #### 3 #### liste des room reservations par CLIENT LASTNAME");
        for (RoomReservation reservation : reservations) {
            System.out.println("\t" + reservation.getId()+
                    " " + reservation.getClient().getFirstname() +
                    " " + reservation.getClient().getLastname() +
                    " " + reservation.getPaymentDate() +
                    " " + reservation.getTotalPrice());
        }

        assertEquals(3, reservations.size());
    }
}
