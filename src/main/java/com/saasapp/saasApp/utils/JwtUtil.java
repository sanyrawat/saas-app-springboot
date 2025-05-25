package com.saasapp.saasApp.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.saasapp.saasApp.service.impl.RedisService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

	@Autowired
	private RedisService redisService;
    private static final long EXPIRATION_MS = 1000*60*60*10; // 1 day
    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Long tenantId) {
    	Map<String, Object> claims = new HashMap<>();
        claims.put("tenantId", tenantId);
        String token = Jwts.builder()
        		.setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
        redisService.addTokenForUser(username, token, EXPIRATION_MS / 1000);
        return token;
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getRemainingValidity(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return Duration.between(LocalDateTime.now(), expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .getSeconds();
    }

}
