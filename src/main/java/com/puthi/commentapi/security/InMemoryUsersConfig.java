package com.puthi.commentapi.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InMemoryUsersConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder enc) {
        var mgr = new InMemoryUserDetailsManager();
        mgr.createUser(User.withUsername("user").password(enc.encode("pass")).roles("USER").build());
        return mgr;
    }
}