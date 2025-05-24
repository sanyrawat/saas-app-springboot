package com.saasapp.saasApp.entity;


import com.saasapp.saasApp.enums.RoleEnum;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleEnum name;  // Example: ADMIN, USER, SUPPORT

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleEnum getName() {
		return name;
	}

	public void setName(RoleEnum name) {
		this.name = name;
	}
    
}
