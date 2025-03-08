package com.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springsecurity.filters.JwtFilter;
import com.springsecurity.services.CustomUserDetailsService;

@Configuration
public class SecurityConfig 
{
    @Autowired
	private CustomUserDetailsService customUserDetailsService;
	
    @Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		return http.
				csrf(csrf->csrf.disable())
				.authorizeHttpRequests(req->
				req.requestMatchers("/auth/**")
				.permitAll()
				.requestMatchers("/admin").hasAuthority("ADMIN")
				.requestMatchers("/user").hasAuthority("USER")
				.anyRequest()
				.authenticated())
				.userDetailsService(customUserDetailsService)
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception
	{
		return auth.getAuthenticationManager();
	}

}
