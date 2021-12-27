package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.response.TokenResponse;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.InvalidToken;
import com.keyword.keywordspring.repository.InvalidTokenRepository;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.JwtUtil;
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
    public TokenResponse generateTokenResponse(AppUser user) {
        return TokenResponse.builder()
                .token(generateJwt(user))
                .refreshToken(generateRefreshToken(user))
                .build();
    }

    @Override
    public Optional<TokenResponse> refreshJwt(String refreshToken) {

        Optional<TokenResponse> response = Optional.empty();

        if(validateRefreshToken(refreshToken)) {

            Optional<AppUser> user = userRepository.findByUsername(getUsernameFromJwt(refreshToken));

            if(user.isPresent()) {
                response = Optional.of(TokenResponse.builder()
                        .token(generateJwt(user.get()))
                        .build());

                if (Objects.requireNonNull(getIssuedAtFromJwt(refreshToken)).getTime() + refreshActionTime < System.currentTimeMillis()) {
                    response.get().setRefreshToken(generateRefreshToken(user.get()));
                    invalidTokenRepository.save(InvalidToken.builder().token(refreshToken).build());
                }
            }
        }

        return response;
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
    public AppUser getUserFromToken(String token) {

        if(null == token)
            return null;

        if(token.startsWith("Bearer "))
            token = token.substring(7);

        Optional<AppUser> user = userRepository.findByUsername(getUsernameFromJwt(token));

        return user.orElse(null);

    }

    @Override
    public String getUsernameFromJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        } catch (Exception ignored) {
            return "";
        }
    }

    @Override
    public String getTypeFromJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody().get("type").toString();
        } catch (Exception ignored) {
            return "";
        }
    }

    private String generateJwt(AppUser user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "token");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private String generateRefreshToken(AppUser user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiryTime))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private boolean validateRefreshToken(String refreshToken) {
        String type = "";

        try {
            type = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken)
                    .getBody().get("type").toString();
        } catch (Exception ignore){}

        Optional<InvalidToken> invalid = invalidTokenRepository.findByToken(refreshToken);

        return Objects.equals(type, "refresh") && invalid.isEmpty();
    }

    private Date getIssuedAtFromJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody()
                    .getIssuedAt();
        } catch (Exception ignored) {
            return null;
        }
    }
}
