package overlook_hotel.overlook_hotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "*/*", "/*", "/css/**", "*/filterForm", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login.loginPage("/login").permitAll());
//        http.authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll() // ✅ allow EVERYTHING
//        )
//                .csrf(csrf -> csrf.disable()) // optional, but makes POST forms easier during dev
//                .formLogin(login -> login.disable()) // disable login page entirely
//                .logout(logout -> logout.disable()); // disable logout handling

        return http.build();
    }
}