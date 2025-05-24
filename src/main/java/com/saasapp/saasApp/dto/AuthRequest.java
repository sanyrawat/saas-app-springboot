package com.saasapp.saasApp.dto;


import lombok.*;

@Data
@Getter 
@Setter
public class AuthRequest {
    private String email;
    private String password;
    private Long tenantId;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
