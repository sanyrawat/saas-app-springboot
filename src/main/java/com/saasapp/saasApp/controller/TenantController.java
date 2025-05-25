package com.saasapp.saasApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saasapp.saasApp.dto.AssignRoleRequest;
import com.saasapp.saasApp.dto.TenantRegisterRequestDto;
import com.saasapp.saasApp.dto.TenantRegisterResponseDto;
import com.saasapp.saasApp.dto.UserResponseDto;
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
    
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping("/assign-role")
	public ResponseEntity<?> assignRole(@RequestBody AssignRoleRequest request) {
		
    	String userEmail = tenantService.assignRoleToUser(request);

		return ResponseEntity.ok("Role " + request.getRole() + " assigned to user " + userEmail);
	}
    
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping("/{tenantId}/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsersByTenant(@PathVariable long tenantId){
    	List<UserResponseDto> response = tenantService.getAllUsersByTenantId(tenantId);
    	
    	return ResponseEntity.ok(response);
    }
}
