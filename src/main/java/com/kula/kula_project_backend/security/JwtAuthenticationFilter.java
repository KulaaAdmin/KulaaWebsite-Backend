package com.kula.kula_project_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * JwtAuthenticationFilter is a filter for JWT authentication.
 * It extends the UsernamePasswordAuthenticationFilter provided by Spring Security.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    /**
     * Constructor for JwtAuthenticationFilter.
     * @param authenticationManager The AuthenticationManager instance.
     * @param tokenProvider The JwtTokenProvider instance.
     */
    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        setAuthenticationManager(authenticationManager);
    }
    /**
     * Attempts to authenticate the user based on the provided request.
     * @param request The HttpServletRequest instance.
     * @param response The HttpServletResponse instance.
     * @return The Authentication instance if authentication is successful.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }
    /**
     * Called when authentication is successful.
     * Adds the JWT to the response's Authorization header.
     * @param request The HttpServletRequest instance.
     * @param response The HttpServletResponse instance.
     * @param chain The FilterChain instance.
     * @param authResult The Authentication instance.
     * @throws IOException if an input or output exception occurs.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String token = tokenProvider.generateToken(authResult);
        response.addHeader("Authorization", "Bearer " + token);
    }
}