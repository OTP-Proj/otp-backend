package org.otp.otp.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.otp.otp.exception.UserNotFoundException;
import org.otp.otp.model.entity.UserEntity;
import org.otp.otp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

/**
 * @author Javidan Alizada
 */
@Component
public class JwtUtil {
    private final HttpUtil httpUtil;
    private final UserRepository repository;
    private final String secretKey;

    public JwtUtil(HttpUtil httpUtil,
                   UserRepository repository,
                   @Value("${jwt.secret}") String secretKey) {
        this.httpUtil = httpUtil;
        this.repository = repository;
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public UserEntity getUserEntityFromToken() {
        var code = this.getUserCodeFromToken();
        return this.repository.findByUserCode(code).orElseThrow(UserNotFoundException::new);
    }

    public UUID getUserIdByCodeFromToken() {
        var code = this.getUserCodeFromToken();
        return repository.findByUserCode(code).map(UserEntity::getId).orElseThrow(UserNotFoundException::new);
    }


    public String getUserRoleByCodeFromToken() {
        var code = this.getUserCodeFromToken();
        return repository.findByUserCode(code)
                .map(UserEntity::getUserRole)
                .map(Enum::name)
                .orElseThrow(UserNotFoundException::new);
    }

    private String getTokenFromHeader() {
        return this.httpUtil.getTokenFromHeader();
    }

    public String getRole() {
        return extractClaim(getTokenFromHeader(), "role");
    }

    public String getUserCodeFromToken() {
        return extractClaim(getTokenFromHeader(), "userCode");
    }

    private String extractClaim(String token, String claimName) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName, String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

}
