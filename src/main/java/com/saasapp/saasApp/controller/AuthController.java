package com.saasapp.saasApp.controller;

import com.saasapp.saasApp.dto.AuthRequest;
import com.saasapp.saasApp.entity.Role;
import com.saasapp.saasApp.entity.Tenant;
import com.saasapp.saasApp.entity.User;
import com.saasapp.saasApp.repository.RoleRepository;
import com.saasapp.saasApp.repository.TenantRepository;
import com.saasapp.saasApp.repository.UserRepository;
import com.saasapp.saasApp.utils.JwtUtil;

import java.util.Set;

import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    public AuthController(
            AuthenticationManager authManager,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            TenantRepository tenantRepository,
            RoleRepository roleRepository
    ) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tenantRepository = tenantRepository;
        this.roleRepository = roleRepository;
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
    	if(userRepo.findByEmail(request.getEmail()).isPresent()) {
    		return ResponseEntity.badRequest().body("User already exist");
    	}
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
     // ✅ Link user to tenant
        Tenant tenant = tenantRepository.findById(request.getTenantId())
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        user.setTenant(tenant);
        Role role = roleRepository.findByName("SUPPORT_ADMIN")
        		.orElseThrow(() -> new RuntimeException("Default Role not found"));
        user.setRoles(Set.of(role));
        userRepo.save(user);
        return ResponseEntity.ok("User registered under Tenant : "+ tenant.getName());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
    	try {
    	    Authentication auth = authManager.authenticate(
    	        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    	    User user = userRepo.findByEmail(request.getEmail())
    	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    	    String token = jwtUtil.generateToken(request.getEmail(), user.getTenant().getId());
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
