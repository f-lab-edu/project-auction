package com.auction.flab.application.config.jwt;

import com.auction.flab.application.vo.AuthVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final Key secretKey;

    public TokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(AuthVo authVo, Duration expiredAt) {
        ZoneId zoneId = ZoneId.systemDefault();
        long currentTime = LocalDateTime.now().atZone(zoneId).toInstant().toEpochMilli();
        Date expiry = new Date(currentTime + expiredAt.toMillis());
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .setSubject(authVo.getEmail())
                .claim("id", authVo.getId())
                .signWith(secretKey)
                .compact();
    }

    public Optional<Authentication> getAuthentication(String token) {
        return getClaims(token).map(claims -> {
            User user = new User(claims.getSubject(), "", Collections.EMPTY_SET);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, token, Collections.EMPTY_SET);
            return authentication;
        }).or(Optional::empty);
    }

    private Optional<Claims> getClaims(String token) {
        try {
            return Optional.ofNullable(Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody());
        } catch (Exception e) {
            log.error("getClaims = {}", e.getMessage());
            return Optional.empty();
        }
    }

}
