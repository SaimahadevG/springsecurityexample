package com.springsecurity.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springsecurity.services.CustomUserDetailsService;
import com.springsecurity.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtFilter extends OncePerRequestFilter
{
   
	
	private  JwtService jwtService;
	
	private CustomUserDetailsService customUserDetailsService;
	
	public JwtFilter(JwtService jwtService,CustomUserDetailsService customUserDetailsService)
	{
		this.jwtService=jwtService;
		this.customUserDetailsService=customUserDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		
		String header = request.getHeader("Authorization");
		
		if(header==null || !header.startsWith("Bearer "))
		{
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = header.substring(7);
		
		String usernameFromToken = jwtService.getUsernameFromToken(token);
		
		if(usernameFromToken!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails userByUsername = customUserDetailsService.loadUserByUsername(usernameFromToken);
			
			
			if(jwtService.isValid(token, userByUsername))
			{
				 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                         userByUsername, null, userByUsername.getAuthorities()
                 );
				 
				   authToken.setDetails(
	                         new WebAuthenticationDetailsSource().buildDetails(request)
	                 );
				   
				   SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
		
		
	}
	
    

}
