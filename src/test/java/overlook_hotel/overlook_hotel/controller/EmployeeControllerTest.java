package overlook_hotel.overlook_hotel.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.model.entity.Job;
import overlook_hotel.overlook_hotel.service.EmployeeService;
import overlook_hotel.overlook_hotel.service.JobService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployeeControllerTest {

    private final EmployeeService mockEmployeeService = mock(EmployeeService.class);
    private final JobService mockJobService = mock(JobService.class);
    private final EmployeeController employeeController = new EmployeeController(mockEmployeeService, mockJobService);
    Job job1 = new Job(1L, "RECEPTIONNISTE");
    Job job2 = new Job(2L, "AGENT_ENTRETIEN");
    Job job3 = new Job(3L, "HOTE");

    @Test
    public void testEmployeeService() {
        Model model = new ConcurrentModel();
        System.out.println("\n\n\n\t\t\t ############## TestEmployeeService début");

        List<Employee> employeesList = Arrays.asList(
                new Employee(1L, "AAAAAAAAH", "Doe", "johndoe@gmail.com", job1 , "pass","salt1"),
                new Employee(2L, "flo", "LEEEEEEEEEEED", "florence@gmail.com", job2 , "ssap","salt2"),
                new Employee(3L, "thibault", "noname", "thibaultnoname@gmail.com", job3 , "password","salt3"));

        when(mockEmployeeService.findAllFiltered(eq(""), eq(""), eq(""), eq(null)))
                .thenReturn(employeesList);

        String employeePage = employeeController.employees("", "", "", null, null, model);

        List<Employee> employees = (List<Employee>) model.getAttribute("rows");

        assertEquals("table", employeePage);

        for (Employee employee : employees) {
            System.out.println(employee.getLastname() + " " + employee.getFirstname());
        }

        assertEquals(3, employees.size());

        System.out.println("\t\t\t ############## TestEmployeeService fin\n\n\n");
    }
}
