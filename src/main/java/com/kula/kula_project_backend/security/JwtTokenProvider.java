package com.kula.kula_project_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 * JwtTokenProvider is a component that provides functionality for JSON Web Token (JWT) creation and validation.
 */
@Component
public class JwtTokenProvider {
    /**
     * The secret key used for JWT creation and validation. It is injected from application properties.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;
    /**
     * The expiration time of the JWT in milliseconds. It is injected from application properties.
     */
    @Value("${jwt.expiration}")
    private long jwtExpirationInMillis;
    /**
     * Constructor for JwtTokenProvider. Prints the JWT secret to the console.
     */
    public JwtTokenProvider() {
        System.out.println("JWT Secret: " + jwtSecret);
    }
    /**
     * Generates a JWT for the authenticated user.
     * @param authentication The Authentication instance containing the user's details.
     * @return A JWT string.
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    /**
     * Extracts the username from the JWT.
     * @param token The JWT string.
     * @return The username string.
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();

        return claims.getSubject();
    }
    /**
     * Validates the JWT.
     * @param authToken The JWT string.
     * @return true if the JWT is valid, false otherwise.
     */
    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Log and handle exceptions
        }
        return false;
    }
}