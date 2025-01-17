package com.example.KorkiMedic.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**")
                .permitAll()
                .requestMatchers("/users/me")
                .hasRole("USER")
                .requestMatchers("/users/fcm-token")
                .hasRole("USER")
                .requestMatchers("/users/updated")
                .hasRole("USER")
                .requestMatchers("/users/change-password")
                .hasRole("USER")
                .requestMatchers("/users/")
                .hasRole("ADMIN")
                .requestMatchers("/api/doctors/**")
                .hasRole("USER")
                .requestMatchers("/api/appointments/doctor")
                .hasRole("DOCTOR")
                .requestMatchers("/api/appointments/{appointmentId}/add-notes")
                .hasRole("DOCTOR")
                .requestMatchers("/api/appointments/**")
                .hasRole("USER")
                .requestMatchers("/api/rewards/**")
                .hasRole("USER")
                .requestMatchers("/api/user-point-actions/**")
                .hasRole("USER")
                .requestMatchers("/api/services/**")
                .hasRole("DOCTOR")
                .requestMatchers("/api/ads/**")
                .hasRole("USER")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8005"));
        configuration.setAllowedMethods(List.of("GET","POST", "PULL"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}