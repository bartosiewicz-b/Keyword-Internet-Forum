package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.LoginResponse;
import com.keyword.keywordspring.exception.InvalidTokenException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.InvalidToken;
import com.keyword.keywordspring.repository.InvalidTokenRepository;
import com.keyword.keywordspring.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtUtilImpl implements JwtUtil {

    @Value("${jwt.secret}")
    private String secret = "secret";
    @Value("${jwt.expiryTime}")
    private Long expiryTime = 1000L;
    @Value("${jwt.refreshExpiryTime}")
    private Long refreshExpiryTime = 1000L;
    @Value("${jwt.refreshActionTime}")
    private Long refreshActionTime = 1000L;

    private final UserRepository userRepository;
    private final InvalidTokenRepository invalidTokenRepository;

    @Override
    public String generateJwt(AppUser user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "token");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    @Override
    public boolean validateJwt(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch(Exception ignored) {
            return false;
        }
    }

    @Override
    public String getUsernameFromJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        } catch (Exception ignored) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public String getTypeFromJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody().get("type").toString();
        } catch (Exception ignored) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public Date getIssuedAtFromJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody()
                    .getIssuedAt();
        } catch (Exception ignored) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public String generateRefresh(AppUser user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiryTime))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    @Override
    public LoginResponse refreshJwt(String refreshToken) {

        try {
            if(validateRefresh(refreshToken)) {

                LoginResponse response = new LoginResponse();

                Optional<AppUser> user = userRepository.findByUsername(getUsernameFromJwt(refreshToken));
                if(user.isPresent()) {
                    response.setToken(generateJwt(user.get()));

                    if (getIssuedAtFromJwt(refreshToken).getTime() + refreshActionTime < System.currentTimeMillis()) {
                        response.setRefreshToken(generateRefresh(user.get()));
                        invalidTokenRepository.save(InvalidToken.builder().token(refreshToken).build());
                    }

                    return response;
                }
        }} catch(Exception ignored) {}

        throw new InvalidTokenException();
    }

    @Override
    public boolean validateRefresh(String refreshToken) {
        try {
            String type = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken)
                    .getBody().get("type").toString();

            Optional<InvalidToken> invalid = invalidTokenRepository.findByToken(refreshToken);

            if(Objects.equals(type, "refresh") && invalid.isEmpty())
                return true;
        } catch(Exception ignored) {}

        return false;
    }
}
