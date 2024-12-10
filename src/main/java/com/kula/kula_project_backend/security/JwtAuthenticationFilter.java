package com.kula.kula_project_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JwtAuthenticationFilter is a filter for JWT authentication.
 * It extends the UsernamePasswordAuthenticationFilter provided by Spring
 * Security.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    /**
     * Constructor for JwtAuthenticationFilter.
     * 
     * @param authenticationManager The AuthenticationManager instance.
     * @param tokenProvider         The JwtTokenProvider instance.
     */
    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;

    }

    protected String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJWTFromRequest(request);     
            try {
                if (token != null && tokenProvider.validateToken(token)) {
                    String username = tokenProvider.getUsernameFromJWT(token);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                System.out.println("Invalid JWT token" + e.getMessage());
            }
            filterChain.doFilter(request, response);
        
    }
}