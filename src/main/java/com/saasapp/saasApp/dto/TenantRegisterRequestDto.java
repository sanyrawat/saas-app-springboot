package com.saasapp.saasApp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter 
@Setter
public class TenantRegisterRequestDto {

	private String tenantName;
	private String tenantEmail;
	private String password;
	public String getTenantEmail() {
		return tenantEmail;
	}
	public void setTenantEmail(String tenantEmail) {
		this.tenantEmail = tenantEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	
}
