package com.puthi.commentapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
public class InMemoryUsers {
    private final Map<String, UserDetails> users = new ConcurrentHashMap<>();

    public InMemoryUsers(PasswordEncoder encoder){
        users.put("user", User.withUsername("user")
                .password(encoder.encode("password"))
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build());
    }

    // simple register API helper
    public void add(String username, String encodedPassword) {
        users.put(username, User.withUsername(username)
                .password(encodedPassword)
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build());
    }

}
