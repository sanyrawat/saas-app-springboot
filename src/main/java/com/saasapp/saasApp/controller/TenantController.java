package com.saasapp.saasApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saasapp.saasApp.entity.Tenant;
import com.saasapp.saasApp.repository.TenantRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantRepository tenantRepository;
    
    public TenantController(TenantRepository tenantRepository) {
    	this.tenantRepository=tenantRepository;
    	
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerTenant(@RequestBody Tenant tenant) {
        if (tenantRepository.findByName(tenant.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Tenant already exists");
        }
        tenant.setActive(true);
        return ResponseEntity.ok(tenantRepository.save(tenant));
    }
}
