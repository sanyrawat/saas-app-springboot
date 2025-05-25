package com.saasapp.saasApp.service.impl;

import java.time.Duration;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private StringRedisTemplate stringRedisTemplate;

	public RedisService(StringRedisTemplate stringRedisTemplate

	) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

    public void saveValue(String key, String value) {
    	stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        return (String) stringRedisTemplate.opsForValue().get(key);
    }
    
    public void blacklistToken(String token, long expirationSeconds) {
    	stringRedisTemplate.opsForValue().set("blacklist:" + token, "true", Duration.ofSeconds(expirationSeconds));
    }

    public boolean isTokenBlacklisted(String token) {
        return "true".equals(stringRedisTemplate.opsForValue().get("blacklist:" + token));
    }

    public void incrementLoginAttempt(String email) {
        String key = "login:attempts:" + email;
        Long attempts = stringRedisTemplate.opsForValue().increment(key);
        stringRedisTemplate.expire(key, Duration.ofMinutes(10));
    }

    public Long getLoginAttempts(String email) {
        String key = "login:attempts:" + email;
        Object value = stringRedisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value.toString()) : 0;
    }

    public void resetLoginAttempts(String email) {
    	stringRedisTemplate.delete("login:attempts:" + email);
    }
    
    public void addTokenForUser(String email, String token, long ttlSeconds) {
        String key = "tokens:" + email;
        stringRedisTemplate.opsForSet().add(key, token);
        stringRedisTemplate.expire(key, Duration.ofSeconds(ttlSeconds));
    }

    public void blacklistAllTokensForAllUsers() {
        ScanOptions scanOptions = ScanOptions.scanOptions().match("tokens:*").build();
        Cursor<byte[]> cursor = stringRedisTemplate.getConnectionFactory()
            .getConnection()
            .scan(scanOptions);

        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            var tokens = stringRedisTemplate.opsForSet().members(key);
            if (tokens != null) {
                for (Object token : tokens) {
                    blacklistToken(token.toString(), 3600); // or use a token-specific TTL if stored
                }
            }
            stringRedisTemplate.delete(key); // Clean up
        }
    }
    
//    @Bean
//    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        return template;
//    }


}
