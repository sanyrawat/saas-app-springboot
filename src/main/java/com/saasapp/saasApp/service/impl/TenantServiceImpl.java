package com.saasapp.saasApp.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saasapp.saasApp.dto.AssignRoleRequest;
import com.saasapp.saasApp.dto.TenantRegisterRequestDto;
import com.saasapp.saasApp.dto.TenantRegisterResponseDto;
import com.saasapp.saasApp.dto.UserResponseDto;
import com.saasapp.saasApp.entity.Role;
import com.saasapp.saasApp.entity.Tenant;
import com.saasapp.saasApp.entity.User;
import com.saasapp.saasApp.enums.RoleEnum;
import com.saasapp.saasApp.repository.RoleRepository;
import com.saasapp.saasApp.repository.TenantRepository;
import com.saasapp.saasApp.repository.UserRepository;
import com.saasapp.saasApp.services.TenantService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService{


    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public TenantServiceImpl(TenantRepository tenantRepository, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.tenantRepository = tenantRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;

	}

    @Transactional
    @Override
    public TenantRegisterResponseDto registerTenant(TenantRegisterRequestDto tenantRegisterRequest) {
        if (userRepository.findByEmail(tenantRegisterRequest.getTenantEmail()).isPresent()) {
            throw new RuntimeException("Tenant email already exists");
        }

        Tenant tenant = new Tenant();
        tenant.setName(tenantRegisterRequest.getTenantName());
        tenant.setActive(true);
        tenant = tenantRepository.save(tenant);

        String baseUsername = tenantRegisterRequest.getTenantEmail().split("@")[0];
        String generatedUsername = baseUsername;
       

        Role role = roleRepository.findByName(RoleEnum.SYSTEM_ADMIN)
            .orElseThrow(() -> new RuntimeException("SYSTEM_ADMIN role not found"));

        User user = new User();
        user.setEmail(tenantRegisterRequest.getTenantEmail());
        user.setUsername(generatedUsername);
        user.setPassword(passwordEncoder.encode(tenantRegisterRequest.getPassword()));
        user.setTenant(tenant);
        user.setRoles(Set.of(role));
        user = userRepository.save(user);

        TenantRegisterResponseDto response = new TenantRegisterResponseDto();
        response.setTenantId(tenant.getId());
        response.setTenantEmail(user.getEmail());
        response.setTenantName(tenant.getName());
        response.setRole(role.getName());

        return response;
    }
    
    @Transactional
    @Override
    public String assignRoleToUser(AssignRoleRequest assignRoleRequest) {
    	User user = userRepository.findById(assignRoleRequest.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Role role = roleRepository.findByName(assignRoleRequest.getRole())
				.orElseThrow(() -> new RuntimeException("Role not found"));
		 user.getRoles().clear();
		user.getRoles().add(role);
		userRepository.save(user);
		return user.getEmail();
    }
    
    @Override
    public List<UserResponseDto> getAllUsersByTenantId(Long tenantId) {
    	List<User> users = userRepository.findByTenantId(tenantId);
    	
    	return users.stream().map(user -> {
    		UserResponseDto userResponseDto = new UserResponseDto();
    		userResponseDto.setEmail(user.getEmail());
    		userResponseDto.setRole( user.getRoles().stream()
                .findFirst() 
                .map(Role::getName)
                .orElse(null));
    		userResponseDto.setUsername(user.getUsername());
    		userResponseDto.setId(user.getId());
    		return userResponseDto;
    	}).toList();
    }
}
