package com.saasapp.saasApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saasapp.saasApp.dto.AssignRoleRequest;
import com.saasapp.saasApp.dto.TenantRegisterRequestDto;
import com.saasapp.saasApp.dto.TenantRegisterResponseDto;
import com.saasapp.saasApp.services.TenantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {


	@Autowired
	private TenantService tenantService;
	
    @PostMapping("/register")
    public ResponseEntity<?> registerTenant(@RequestBody TenantRegisterRequestDto tenantRegisterRequest) {
        
        TenantRegisterResponseDto response = tenantService.registerTenant(tenantRegisterRequest);
        
        	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/assign-role")
	public ResponseEntity<?> assignRole(@RequestBody AssignRoleRequest request) {
		
    	String userEmail = tenantService.assignRoleToUser(request);

		return ResponseEntity.ok("Role " + request.getRole() + " assigned to user " + userEmail);
	}
}
