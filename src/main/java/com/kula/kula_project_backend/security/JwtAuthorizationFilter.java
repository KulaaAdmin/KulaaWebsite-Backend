package com.kula.kula_project_backend.security;

import com.kula.kula_project_backend.entity.*;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kula.kula_project_backend.dao.RoleRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.jsonwebtoken.ExpiredJwtException;

/**
 * JwtAuthorizationFilter is a filter for JWT authorization.
 * It extends the BasicAuthenticationFilter provided by Spring Security.
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
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
        super();
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        String roleId = null;

        try {
            roleId = Jwts.parser()
                    .setSigningKey(tokenProvider.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roleId", String.class);
        } catch (SignatureException e) {
            System.out.println("JWT signature does not match.");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT expired.");
        }
        List<SimpleGrantedAuthority> authorities ;
        if (roleId != null) {
            Role role = roleRepository.findById(new ObjectId(roleId))
                    .orElseThrow(() -> new RuntimeException("Role not found"));

                    authorities= getAuthorities(role.getPermissions());


        }else {
            authorities=new ArrayList<>();
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(null, null,
                authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);

    }

    private List<SimpleGrantedAuthority> getAuthorities(HashMap<String, Boolean> permissions) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        permissions.forEach((key, value) -> {
            if (Boolean.TRUE.equals(value)) {
                authorities.add(new SimpleGrantedAuthority(key));
            }
        });
        return authorities;
    }
}
