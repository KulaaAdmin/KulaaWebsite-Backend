package com.kula.kula_project_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JwtAuthorizationFilter is a filter for JWT authorization.
 * It extends the BasicAuthenticationFilter provided by Spring Security.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtTokenProvider tokenProvider;

    /**
     * Constructor for JwtAuthorizationFilter.
     * 
     * @param authenticationManager The AuthenticationManager instance.
     * @param tokenProvider         The JwtTokenProvider instance.
     */
    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    /**
     * Filters requests to check for a valid JWT in the Authorization header.
     * If the token is valid, it sets the authentication in the context to specify
     * that the current user is authenticated.
     * 
     * @param request  The HttpServletRequest instance.
     * @param response The HttpServletResponse instance.
     * @param chain    The FilterChain instance.
     * @throws IOException      if an input or output exception occurs.
     * @throws ServletException if a servlet exception occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                if (tokenProvider.validateToken(token)) {
                    String username = tokenProvider.getUsernameFromJWT(token);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                System.out.println("Invalid JWT token" + e.getMessage());
            }
        }
        chain.doFilter(request, response);
       
    }
}
