package com.springsecurity.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurity.dtos.JwtRequest;
import com.springsecurity.dtos.JwtResponse;
import com.springsecurity.entities.User;
import com.springsecurity.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController 
{
	@Autowired
	private AuthService authService;
	
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
    {
    	JwtResponse login = authService.login(request);
    	return ResponseEntity.ok(login);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user)
    {
       User user2 = authService.signUp(user);
       return ResponseEntity.status(HttpStatus.CREATED).body(user2);
    }
}
