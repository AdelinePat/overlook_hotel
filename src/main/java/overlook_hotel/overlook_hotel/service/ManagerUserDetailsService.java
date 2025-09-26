package overlook_hotel.overlook_hotel.service;

import overlook_hotel.overlook_hotel.model.entity.Manager;
import overlook_hotel.overlook_hotel.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class ManagerUserDetailsService implements UserDetailsService {
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByEmail(email);
        if (manager == null) {
            throw new UsernameNotFoundException("Manager not found");
        }
        return User.builder()
                .username(manager.getEmail())
                .password(manager.getPassword())
                .roles("MANAGER")
                .build();
    }
}
