package overlook_hotel.overlook_hotel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.model.entity.Job;

import java.util.List;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    private final Job job = new Job(1L, "RECEPTIONNISTE");

    @Test
    public void findAllEmployees() {
        List<Employee> employees = employeeService.findAllFiltered("", "", "", null);
        System.out.println("\t\t #### 1 #### liste des employees:");
        for (Employee employee : employees) {
            System.out.println("\t" + employee.getLastname() + " " + employee.getFirstname());
        }
        Assertions.assertEquals(11, employees.size());
    }

    @Test
    public void findEmployeeByLastname() {
        List<Employee> employees = employeeService.findAllFiltered("Me", "", "", null);
        System.out.println("\t\t #### 2 #### liste des employees nom:");
        for (Employee employee : employees) {
            System.out.println("\t" + employee.getLastname() + " " + employee.getFirstname());
        }
        Assertions.assertEquals(2, employees.size());
    }

    @Test
    public void findEmployeeByLastnameAndFirstname() {
        List<Employee> employees = employeeService.findAllFiltered("Me", "e", "", null);
        System.out.println("\t\t #### 3 #### liste des employees nom prénom:");
        for (Employee employee : employees) {
            System.out.println("\t" + employee.getLastname() + " " + employee.getFirstname());
        }
        Assertions.assertEquals(2, employees.size());
    }

    @Test
    public void findEmployeeByJob() {
        List<Employee> employees = employeeService.findAllFiltered("", "", "", this.job);
        System.out.println("\t\t #### 4 #### liste des employees JOB:");
        for (Employee employee : employees) {
            System.out.println("\t" + employee.getLastname() + " " + employee.getFirstname() + " " + employee.getJob());
        }
        Assertions.assertEquals(2, employees.size());
    }
}
