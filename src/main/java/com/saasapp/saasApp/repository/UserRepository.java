package com.saasapp.saasApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saasapp.saasApp.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User,Long>{
	Optional<User> findByEmail(String emailsss);

}
