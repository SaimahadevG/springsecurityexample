package com.springsecurity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springsecurity.dtos.JwtRequest;
import com.springsecurity.dtos.JwtResponse;
import com.springsecurity.entities.User;
import com.springsecurity.repositories.UserRepo;


@Service
public class AuthService 
{
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public JwtResponse login(JwtRequest request) {
		try
		{
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		}
		catch (BadCredentialsException e) {
			throw new BadCredentialsException("wrong username or password");
		}
		
		UserDetails userByUsername = customUserDetailsService.loadUserByUsername(request.getEmail());
		String token = jwtService.generateToken(userByUsername);
		return new JwtResponse(userByUsername.getUsername(), token);
	}

	public User signUp(User user) 
	{
		  
	      String password = user.getPassword();
	      String encode = passwordEncoder.encode(password);
	      user.setPassword(encode);
	      User save = userRepo.save(user);
	       return save;
	}
   
}
