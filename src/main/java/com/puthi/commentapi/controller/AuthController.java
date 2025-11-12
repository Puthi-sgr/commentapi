package com.puthi.commentapi.controller;

import com.puthi.commentapi.security.InMemoryUsers;
import com.puthi.commentapi.dto.auth.LoginRequest;
import com.puthi.commentapi.dto.auth.TokenResponse;
import com.puthi.commentapi.security.JwtService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwt;
    private final InMemoryUsers inMemoryUsers;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager am, JwtService jwt,
                          InMemoryUsers users, PasswordEncoder encoder) {
        this.authManager = am;
        this.jwt = jwt;
        this.inMemoryUsers = users;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req){
        var auth = new UsernamePasswordAuthenticationToken(req.username(), req.password());
        authManager.authenticate(auth); // throws if bad creds
        String token = jwt.generate(req.username());
        return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest req){
        inMemoryUsers.add(req.username(), encoder.encode(req.password()));
        return ResponseEntity.ok().build();
    }
}
