package com.saasapp.saasApp.dto;

import com.saasapp.saasApp.entity.Role;
import com.saasapp.saasApp.enums.RoleEnum;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter 
@Setter
public class AssignRoleRequest {
    private Long userId;
    private RoleEnum role;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public RoleEnum getRole() {
		return role;
	}
	public void setRole(RoleEnum role) {
		this.role = role;
	}
    
}
