package com.puthi.commentapi.config;

import com.puthi.commentapi.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration /// So my boy Spring knows this is a config class gonna scanned beans will be replaced
public class SecurityConfig {

    //Spring will look for the secruityFilterchain bean and override the default security settings
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/actuator/health","/api/auth/**").permitAll()
                .anyRequest().authenticated()
        );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager(); // lazy, avoids cycles
    }
}