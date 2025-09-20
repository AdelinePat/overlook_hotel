package overlook_hotel.overlook_hotel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import overlook_hotel.overlook_hotel.model.entity.Room;
import overlook_hotel.overlook_hotel.model.entity.Standing;
import overlook_hotel.overlook_hotel.model.enumList.BedType;
import overlook_hotel.overlook_hotel.model.enumList.RoomBonusEnum;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RoomServiceTest {
    private final Standing roomStanding = new Standing(1L, "DE_LUXE");;
    private final BedType type = BedType.SIMPLE;
    private final List<Integer> night_price_null = Arrays.asList(null, null);
    private final List<Integer> night_price_lower = Arrays.asList(100, null);
    private final List<Integer> night_price_greater = Arrays.asList(null, 180);
    private final List<Integer> night_price_between = Arrays.asList(100, 180);
    private final LocalDate startDate = LocalDate.of(2025, 9, 6);
    private final LocalDate endDate = LocalDate.of(2025, 9, 8);
    private final RoomBonusEnum bonus = RoomBonusEnum.MINI_BAR;

    @Autowired
    private RoomService roomService;

    @Test
    public void findAllRooms() {
        List<Room> rooms = roomService.findAllFiltered(null, null, null, null, null, null, null, this.night_price_null, null);

        assertEquals(10, rooms.size());
        System.out.println("\t\t #### 1 #### liste des chambres");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getNightPrice());
        }
    }

    @Test
    public void findByNumber() {
        List<Room> rooms = roomService.findAllFiltered(105, null, null, null, null, null, null, null, null);

        assertEquals(1, rooms.size());
        System.out.println("\t\t #### 2 #### liste des chambres par nombre");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getNightPrice());
        }
    }

    @Test
    public void findByCapacity() {
        List<Room> rooms = roomService.findAllFiltered(null, 2, null, null, null, null, null, null, null);

        assertEquals(6, rooms.size());
        System.out.println("\t\t #### 3 #### liste des chambres capacité");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getNightPrice() + " " + room.getCapacity());
        }
    }

    @Test
    public void findByStanding() {
        List<Room> rooms = roomService.findAllFiltered(null, null, null, this.roomStanding, null, null, null, null, null);

        assertEquals(3, rooms.size());
        System.out.println("\t\t #### 4 #### liste des chambres standing");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getNightPrice());
        }
    }

    @Test
    public void findByBedType() {
        List<Room> rooms = roomService.findAllFiltered(null, null, null, null, this.type, null, null, null, null);

        assertEquals(3, rooms.size());
        System.out.println("\t\t #### 4 #### liste des chambres type");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getNightPrice() + " " + room.getType());
        }
    }

    @Test
    public void findByLowerThan() {
        List<Room> rooms = roomService.findAllFiltered(null, null, null, null, null, null, null, night_price_lower, null);

        assertEquals(3, rooms.size());
        System.out.println("\t\t #### 5.1 #### liste des chambres prix LOWER");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getNightPrice());
        }
    }

    @Test
    public void findByGreaterThan() {
        List<Room> rooms = roomService.findAllFiltered(null, null, null, null, null, null,null, night_price_greater, null);

        assertEquals(2, rooms.size());
        System.out.println("\t\t #### 5.2 #### liste des chambres prix GREATER");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getNightPrice());
        }
    }

    @Test
    public void findByBetween() {
        List<Room> rooms = roomService.findAllFiltered(null, null, null, null, null, null, null, night_price_between, null);

        assertEquals(5, rooms.size());
        System.out.println("\t\t #### 5.3 #### liste des chambres prix BETWEEN");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getNightPrice());
        }
    }

    @Test
    public void findByDescriptionContent() {
        List<Room> rooms = roomService.findAllFiltered(null, null, "with", null, null, null, null, null, null);

        assertEquals(5, rooms.size());
        System.out.println("\t\t #### 6 #### liste des chambres description");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getDescription()) ;
        }
    }

    @Test
    public void findByAvailability() {
        List<Room> rooms = roomService.findAllFiltered(null, null, null, null, null, this.startDate, this.endDate, null, null);

        assertEquals(4, rooms.size());
        System.out.println("\t\t #### 7 #### liste des chambres DISPONIBLE du 6 au 8 septembre");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getDescription()) ;
        }
    }

    @Test
    public void findByBonus() {
        List<Room> rooms = roomService.findAllFiltered(null, null, null, null, null, null, null, null, this.bonus);

        assertEquals(2, rooms.size());
        System.out.println("\t\t #### 8 #### liste des chambres SECHOIR");
        for (Room room : rooms) {
            System.out.println("\t" + room.getNumber() + " " + room.getStanding().getName() + " " + room.getDescription()) ;
        }
    }


}
