package com.saasapp.saasApp.dto;

import com.saasapp.saasApp.enums.RoleEnum;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter 
@Setter
public class TenantRegisterResponseDto {
	private long tenantId;
	private String tenantName;
	private String tenantEmail;
	private RoleEnum role;
	public long getTenantId() {
		return tenantId;
	}
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getTenantEmail() {
		return tenantEmail;
	}
	public void setTenantEmail(String tenantEmail) {
		this.tenantEmail = tenantEmail;
	}
	public RoleEnum getRole() {
		return role;
	}
	public void setRole(RoleEnum role) {
		this.role = role;
	}

}
