package overlook_hotel.overlook_hotel.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import overlook_hotel.overlook_hotel.entity.Manager;

import java.util.List;

@SpringBootTest
public class ManagerServiceTest {

    @Autowired
    private ManagerService managerService;

    @Test
    public void findAllManagers() {
        List<Manager> managers = managerService.findAllFiltered("", "", "");
        Assertions.assertEquals(2, managers.size());
        System.out.println("\t\t #### 1 #### liste des manager");
        for (Manager manager : managers) {
            System.out.println("\t" + manager.getFirstname() + " " + manager.getLastname());
        }
    }

    @Test
    public void findByEmail() {
        List<Manager> managers = managerService.findAllFiltered("", "", "n@e");
        Assertions.assertEquals(1, managers.size());
        System.out.println("\t\t #### 2 #### liste des manager EMAIL");
        for (Manager manager : managers) {
            System.out.println("\t" + manager.getFirstname() + " " + manager.getLastname());
        }
    }
}
