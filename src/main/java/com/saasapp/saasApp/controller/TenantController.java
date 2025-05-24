package com.saasapp.saasApp.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saasapp.saasApp.dto.AssignRoleRequest;
import com.saasapp.saasApp.dto.TenantRegisterRequestDto;
import com.saasapp.saasApp.dto.TenantRegisterResponseDto;
import com.saasapp.saasApp.entity.Role;
import com.saasapp.saasApp.entity.Tenant;
import com.saasapp.saasApp.entity.User;
import com.saasapp.saasApp.enums.RoleEnum;
import com.saasapp.saasApp.repository.RoleRepository;
import com.saasapp.saasApp.repository.TenantRepository;
import com.saasapp.saasApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    
	public TenantController(TenantRepository tenantRepository, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.tenantRepository = tenantRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;

	}

    @PostMapping("/register")
    public ResponseEntity<?> registerTenant(@RequestBody TenantRegisterRequestDto tenantRegisterRequest) {
        if (userRepository.findByEmail(tenantRegisterRequest.getTenantEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Tenant email already exists");
        }
        Tenant tenant = new Tenant();
        tenant.setName(tenantRegisterRequest.getTenantName());
        tenant.setActive(true);
        tenant = tenantRepository.save(tenant);
     // Fetch SYSTEM_ADMIN role
        Role role = roleRepository.findByName(RoleEnum.SYSTEM_ADMIN)
            .orElseThrow(() -> new RuntimeException("SYSTEM_ADMIN role not found"));
        User user = new User();
        user.setEmail(tenantRegisterRequest.getTenantEmail());
        user.setPassword(passwordEncoder.encode(tenantRegisterRequest.getPassword()));
        user.setTenant(tenant);
        user.setRoles(Set.of(role));
        user = userRepository.save(user);
        TenantRegisterResponseDto response = new TenantRegisterResponseDto();
        response.setTenantId(tenant.getId());
        response.setTenantEmail(user.getEmail());
        response.setTenantName(tenant.getName());
        response.setRole(role.getName());
        	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/assign-role")
	public ResponseEntity<?> assignRole(@RequestBody AssignRoleRequest request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Role role = roleRepository.findByName(request.getRole())
				.orElseThrow(() -> new RuntimeException("Role not found"));
		user.getRoles().add(role);
		userRepository.save(user);

		return ResponseEntity.ok("Role " + role.getName() + " assigned to user " + user.getEmail());
	}
}
