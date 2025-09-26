package overlook_hotel.overlook_hotel.service;

import overlook_hotel.overlook_hotel.model.entity.Manager;
import overlook_hotel.overlook_hotel.model.entity.Client;
import overlook_hotel.overlook_hotel.model.entity.Employee;
import overlook_hotel.overlook_hotel.repository.ManagerRepository;
import overlook_hotel.overlook_hotel.repository.ClientRepository;
import overlook_hotel.overlook_hotel.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByEmail(email);
        if (manager != null) {
            return User.builder()
                    .username(manager.getEmail())
                    .password(manager.getPassword())
                    .roles("MANAGER")
                    .build();
        }
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            return User.builder()
                    .username(client.getEmail())
                    .password(client.getPassword())
                    .roles("CLIENT")
                    .build();
        }
        Employee employee = employeeRepository.findByEmail(email);
        if (employee != null) {
            return User.builder()
                    .username(employee.getEmail())
                    .password(employee.getPassword())
                    .roles("EMPLOYEE")
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
