package overlook_hotel.overlook_hotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@Configuration
public class SecurityConfig {
    // Removed unused customUserDetailsService field

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    // Manager filter chain: handles /login-admin and manager-only endpoints
    @Bean
    @Order(1)
    public SecurityFilterChain managerFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/login-admin/**", "/clients/**", "/employees/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login-admin", "/login-admin/**").permitAll()
                .requestMatchers("/clients/**", "/employees/**").hasRole("MANAGER")
                .anyRequest().denyAll()
            )
            .formLogin(login -> login
                .loginPage("/login-admin")
                .defaultSuccessUrl("/clients", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login-admin?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());
        return http.build();
    }

    // Default filter chain: handles all other users
    @Bean
    @Order(2)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            .authorizeHttpRequests(auth -> auth
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