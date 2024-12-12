package com.kula.kula_project_backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.kula.kula_project_backend.entity.Users.UserType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JwtAuthorizationFilter is a filter for JWT authorization.
 * It extends the BasicAuthenticationFilter provided by Spring Security.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtTokenProvider tokenProvider;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

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
    // @Override
    // protected void doFilterInternal(HttpServletRequest request,
    // HttpServletResponse response, FilterChain chain)
    // throws IOException, ServletException {
    // String token = request.getHeader("Authorization");
    // if (token != null && token.startsWith("Bearer ")) {
    // try {
    // if (tokenProvider.validateToken(token)) {
    // String userTypeFromToken = tokenProvider.getUserTypeFromJWT(token);
    // UserType userType = UserType.valueOf(userTypeFromToken);//change String into
    // enum
    // List<GrantedAuthority> authorities = getAuthoritiesForUserType(userType);

    // String username = tokenProvider.getUsernameFromJWT(token);
    // //String userType = tokenProvider.getUserTypeFromJWT(token);
    // UsernamePasswordAuthenticationToken authentication =
    // new UsernamePasswordAuthenticationToken(username, null, authorities);

    // SecurityContextHolder.getContext().setAuthentication(authentication);
    // }
    // } catch (Exception e) {
    // System.out.println("Invalid JWT token" + e.getMessage());
    // }
    // }
    // chain.doFilter(request, response);

    // }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = request.getHeader("Authorization");
        log.info("------------------------------");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                if (tokenProvider.validateToken(token)) {
                    // String userTypeFromToken = tokenProvider.getUserTypeFromJWT(token);
                    String userType = tokenProvider.getUserTypeFromJWT(token);
                    System.out.println("------------------------ " + userType);
                    log.info("----------------------------------- userType: {}", userType);
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userType));

                    String username = tokenProvider.getUsernameFromJWT(token);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);

                   
                    if (SecurityContextHolder.getContext().getAuthentication() != null) {
                        System.out.println("User: " + SecurityContextHolder.getContext().getAuthentication().getName());
                        log.info("User: " + SecurityContextHolder.getContext().getAuthentication().getName());
                        System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                        log.info("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                    }

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                System.out.println("Invalid JWT token: " + e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }

}
