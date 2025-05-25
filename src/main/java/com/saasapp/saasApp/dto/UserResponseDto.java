package com.saasapp.saasApp.dto;

import com.saasapp.saasApp.enums.RoleEnum;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter 
@Setter
public class UserResponseDto {
	 private Long id;
	    private String email;
	    private String username;
	    private RoleEnum role;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public RoleEnum getRole() {
			return role;
		}
		public void setRole(RoleEnum role) {
			this.role = role;
		}
	    
}
