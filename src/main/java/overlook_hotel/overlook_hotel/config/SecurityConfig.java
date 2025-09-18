package overlook_hotel.overlook_hotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Actuator ouvert
                        .requestMatchers("/actuator/**").permitAll()
                        // assets / pages ouvertes
                        .requestMatchers("/", "/clients/**", "/employees/**",
                                "/css/**", "/js/**", "/images/**").permitAll()
                        // et tout le reste aussi ouvert (pour l’instant)
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> {})     // optionnel
                .logout(logout -> logout.disable());

        return http.build();
    }
}
