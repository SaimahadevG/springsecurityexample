package com.springsecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.springsecurity.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	UserDetails findByEmail(String username);

}
