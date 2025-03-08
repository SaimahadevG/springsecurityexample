package com.springsecurity.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springsecurity.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService 
{
	 private final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";
	 
	 public String getUsernameFromToken(String token)
	 {
		 return extractClaim(token,Claims::getSubject);
	 }
	 
	 public Date getExpiryDateFromToken(String token)
	 {
		 return  extractClaim(token,Claims::getExpiration);
	 }

	private <T> T extractClaim(String token, Function<Claims,T> resolver) {
		
		Claims claims=getAllClaims(token);
		return resolver.apply(claims);
		
	}

	private Claims getAllClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	private SecretKey getSigningKey() {
		byte[] decode = Decoders.BASE64URL.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(decode);
	}
	
	public String generateToken(UserDetails userByUsername)
	{ Map<String, Object> claims = new HashMap<>();
    
		return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userByUsername.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(getSigningKey())
                .compact();
	}
	
	
	public boolean isValid(String token,UserDetails user)
	{
		String usernameFromToken = getUsernameFromToken(token);
		return usernameFromToken.equals(user.getUsername()) && !isTokenExpired(token);
	}
	
	 private boolean isTokenExpired(String token) {
	        return getExpiryDateFromToken(token).before(new Date());
	    }

	
	 
 
	 
	 
}
