package com.example.demo.security.jwt;

import com.example.demo.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${projeto.jwtSecret}")
    private String jwtSecret;

    @Value("${projeto.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateTokenFromUserDetailsImpl(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getSigninKey(), Jwts.SIG.HS512).compact();
    }

    public SecretKey getSigninKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigninKey())
                    .build()
                    .parseSignedClaims(authToken)
                    .getPayload();
            return true;
        } catch (MalformedJwtException e) {
            logger.warn("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.warn("Token JWT expirado: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("Assinatura JWT inválida: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Token JWT vazio ou inválido: {}", e.getMessage());
        }

        return false;
    }

    public String getUsernameToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
