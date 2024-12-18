package com.example.OpendData;
    
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Configuration de la sécurité
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests((authorizeRequests) -> 
        authorizeRequests
            .requestMatchers("/", "/register", "/login", "/js/**", "/css/**", "/Images/**","/search/**","/search","/wording/**").permitAll()
            .requestMatchers("/account", "/account/update_email", "/account/update_password","/add","/wording/edit/**", "/load").authenticated()
    ).csrf(csrf -> csrf.disable())
    .logout(logout -> logout
                        .logoutUrl("/logout") // L'URL de déconnexion
                        .logoutSuccessUrl("/") // Redirection après déconnexion
                        .invalidateHttpSession(true) // Invalidation de la session
                        .clearAuthentication(true) // Effacer l'authentification
                        .permitAll())
                .sessionManagement(management -> management
                        .invalidSessionUrl("/?session=expired") // Redirection si la session expire
                        .maximumSessions(1) // Limite une seule session par utilisateur
                        .expiredUrl("/?session=expired"));
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
