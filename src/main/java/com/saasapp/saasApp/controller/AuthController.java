package com.saasapp.saasApp.controller;

import com.saasapp.saasApp.dto.AuthRequest;
import com.saasapp.saasApp.entity.User;
import com.saasapp.saasApp.repository.UserRepository;
import com.saasapp.saasApp.utils.JwtUtil;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public AuthController(
            AuthenticationManager authManager,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
    	try {
    	    Authentication auth = authManager.authenticate(
    	        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    	    String token = jwtUtil.generateToken(request.getEmail());
    	    return ResponseEntity.ok(token);
    	} catch (BadCredentialsException ex) {
    	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    	}
    }
    
    @GetMapping("/secure-data")
    public ResponseEntity<String> secured() {
    	var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        return ResponseEntity.ok("Access granted to secure data 🔐");
    }

}
