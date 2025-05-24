package com.saasapp.saasApp.controller;

import com.saasapp.saasApp.dto.AuthRequest;
import com.saasapp.saasApp.services.LoginService;
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
