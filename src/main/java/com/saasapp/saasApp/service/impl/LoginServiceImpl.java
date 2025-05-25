package com.saasapp.saasApp.service.impl;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saasapp.saasApp.dto.AuthRequest;
import com.saasapp.saasApp.entity.Tenant;
import com.saasapp.saasApp.entity.User;
import com.saasapp.saasApp.repository.RoleRepository;
import com.saasapp.saasApp.repository.TenantRepository;
import com.saasapp.saasApp.repository.UserRepository;
import com.saasapp.saasApp.services.LoginService;
import com.saasapp.saasApp.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
	private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TenantRepository tenantRepository;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    public LoginServiceImpl(
            AuthenticationManager authManager,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            TenantRepository tenantRepository,
            RoleRepository roleRepository,
            RedisService redisService
    ) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tenantRepository = tenantRepository;
        this.redisService = redisService;
    }
    
    @Override
    public String register(AuthRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        user.setTenant(tenant);

        // Optional: Assign default role here if needed
        userRepo.save(user);

        return "User registered under Tenant: " + tenant.getName();
    }

    @Override
    public String login(AuthRequest request) {
    	String email = request.getEmail();
    	if (redisService.getLoginAttempts(email) >= 5) {
            throw new RuntimeException("Account locked due to too many failed attempts");
        }
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            redisService.resetLoginAttempts(email);

            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return jwtUtil.generateToken(request.getEmail(), user.getTenant().getId());
        } catch (BadCredentialsException ex) {
        	redisService.incrementLoginAttempt(email);
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
