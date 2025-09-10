package com.stacklab.gxp.web;

import com.stacklab.gxp.domain.AppUser;
import com.stacklab.gxp.domain.Role;
import com.stacklab.gxp.repo.UserRepository;
import com.stacklab.gxp.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder,
                          AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        AppUser u = new AppUser();
        u.setUsername(payload.get("username"));
        u.setPassword(encoder.encode(payload.get("password")));
        if ("ADMIN".equalsIgnoreCase(payload.getOrDefault("role", "USER"))) {
            u.setRole(Role.ADMIN);
        } else {
            u.setRole(Role.USER);
        }
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("status", "registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.get("username"), payload.get("password")));
        String token = jwtUtil.generateToken(payload.get("username"),
                auth.getAuthorities().iterator().next().getAuthority());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
