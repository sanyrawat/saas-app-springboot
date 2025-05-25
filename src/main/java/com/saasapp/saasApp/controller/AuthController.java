package com.saasapp.saasApp.controller;

import com.saasapp.saasApp.dto.AuthRequest;
import com.saasapp.saasApp.service.impl.RedisService;
import com.saasapp.saasApp.services.LoginService;
import com.saasapp.saasApp.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
    	 try {
             String message = loginService.register(request);
             return ResponseEntity.ok(message);
         } catch (RuntimeException ex) {
             return ResponseEntity.badRequest().body(ex.getMessage());
         }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
    	try {
            String token = loginService.login(request);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
            }
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login error: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }
    }
    
    @GetMapping("/secure-data")
    public ResponseEntity<String> secured() {
    	var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        return ResponseEntity.ok("Access granted to secure data 🔐");
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid token");
        }

        String token = authHeader.substring(7);
        long expiry = jwtUtil.getRemainingValidity(token); // ⏳ method you’ll implement below

        redisService.blacklistToken(token, expiry);
        return ResponseEntity.ok("Successfully logged out");
    }
    
    @PostMapping("/logout-all")
    public ResponseEntity<String> logoutAll(HttpServletRequest request) {
        
        redisService.blacklistAllTokensForAllUsers();
        return ResponseEntity.ok("All sessions for user have been logged out");
    }



}
