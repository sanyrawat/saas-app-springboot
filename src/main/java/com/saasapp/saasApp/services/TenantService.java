package com.saasapp.saasApp.services;

import com.saasapp.saasApp.dto.AssignRoleRequest;
import com.saasapp.saasApp.dto.TenantRegisterRequestDto;
import com.saasapp.saasApp.dto.TenantRegisterResponseDto;

public interface TenantService {

	TenantRegisterResponseDto registerTenant(TenantRegisterRequestDto tenantRegisterRequest);

	String assignRoleToUser(AssignRoleRequest assignRoleRequest);

}
