package com.keyword.keywordspring.service;

import com.keyword.keywordspring.exception.InvalidTokenException;
import com.keyword.keywordspring.model.AppUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtilImpl implements JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiryTime}")
    private Integer expiryTime;

    @Override
    public String generateJwt(AppUser user) {
        return doGenerateToken(new HashMap<>(), user.getUsername());
    }

    @Override
    public boolean validateJwt(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch(SignatureException | MalformedJwtException |
                UnsupportedJwtException | IllegalArgumentException |
                ExpiredJwtException e) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public String getUsernameFromJwt(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
