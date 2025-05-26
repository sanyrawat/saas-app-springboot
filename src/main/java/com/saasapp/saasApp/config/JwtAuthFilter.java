package com.saasapp.saasApp.config;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saasapp.saasApp.service.impl.CustomUserDetailsService;
import com.saasapp.saasApp.service.impl.RedisService;
import com.saasapp.saasApp.utils.JwtUtil;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	 @Autowired
	    private JwtUtil jwtUtil;

	    @Autowired
	    private CustomUserDetailsService userDetailsService;
	    
	    @Autowired
	    private RedisService redisService;
	    @Override
	    protected void doFilterInternal(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    FilterChain filterChain)
	            throws ServletException, IOException {

	        final String authHeader = request.getHeader("Authorization");
	        final String token;
	        final String username;

	        // ✅ Defensive check to skip empty or malformed tokens
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        token = authHeader.substring(7); // Extract token after "Bearer "

	        // ✅ Additional safe check to avoid parsing empty tokens
	        if (token.trim().isEmpty()) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        try {
	            username = jwtUtil.extractUsername(token);
	        } catch (Exception e) {
	            // ✅ Log and ignore bad token
	            System.out.println("Invalid JWT: " + e.getMessage());
	            filterChain.doFilter(request, response);
	            return;
	        }

	        // Continue authentication if valid
	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            var userDetails = userDetailsService.loadUserByUsername(username);
	            if (jwtUtil.validateToken(token)) {
	                var authToken = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities());
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }
	        filterChain.doFilter(request, response);
	    }

}
