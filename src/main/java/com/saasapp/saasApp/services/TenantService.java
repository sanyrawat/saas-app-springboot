package com.saasapp.saasApp.services;

import java.util.List;

import com.saasapp.saasApp.dto.AssignRoleRequest;
import com.saasapp.saasApp.dto.TenantRegisterRequestDto;
import com.saasapp.saasApp.dto.TenantRegisterResponseDto;
import com.saasapp.saasApp.dto.UserResponseDto;

public interface TenantService {

	TenantRegisterResponseDto registerTenant(TenantRegisterRequestDto tenantRegisterRequest);

	String assignRoleToUser(AssignRoleRequest assignRoleRequest);

	List<UserResponseDto> getAllUsersByTenantId(Long tenantId);

}
