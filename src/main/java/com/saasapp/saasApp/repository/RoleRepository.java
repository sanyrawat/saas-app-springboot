package com.saasapp.saasApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saasapp.saasApp.entity.Role;
import com.saasapp.saasApp.enums.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByName(RoleEnum systemAdmin);

}
