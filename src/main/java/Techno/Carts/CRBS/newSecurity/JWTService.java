package Techno.Carts.CRBS.newSecurity;

import Techno.Carts.CRBS.Entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;

import org.springframework.stereotype.Service;



import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;


import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private final String secretKey;

    // Recommended: put this in application.properties / application.yml
    // jwt.secret=your-very-long-random-base64-string-here (at least 256 bits → 32 bytes → 44 chars in base64)
    public JWTService(@Value("${jwt.secret:}") String configuredSecret) {
        if (configuredSecret != null && !configuredSecret.trim().isEmpty()) {
            this.secretKey = configuredSecret;
        } else {
            // Fallback – **only for development / testing**
            // In production → must come from configuration / secret manager
            this.secretKey = generateRandomBase64Secret();
        }
    }

    private String generateRandomBase64Secret() {
        try {
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT secret key", e);
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // You can add more claims if needed, e.g.:
         claims.put("role", user.getRole());
        claims.put("Id", user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Here using 24 hours – adjust to your needs
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .signWith(getSigningKey())           // modern & type-safe way
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            String actualUsername = extractUsername(token);
            return actualUsername.equals(expectedUsername)
                    && !isTokenExpired(token);
        } catch (Exception e) {

            return false;
        }
    }

    boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }


    public String getSecretKeyForDebug() {
        return secretKey;
    }
}