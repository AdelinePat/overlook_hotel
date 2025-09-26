package overlook_hotel.overlook_hotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@Configuration
public class SecurityConfig {
    @Autowired
    private overlook_hotel.overlook_hotel.service.CustomUserDetailsService customUserDetailsService;

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "*/*", "/*", "/css/**", "*/filterForm", "/js/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login.loginPage("/login").permitAll());

        http
            .userDetailsService(customUserDetailsService)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/clients", "/employees").hasRole("MANAGER")
                .requestMatchers("/cart", "/room_reservation", "/room-detail", "/event-reservation").hasRole("CLIENT")
                .requestMatchers("/login", "/css/**", "/js/**", "/img/**", "/").permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}