package com.stocks.register.api.configuration;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stocks.register.api.models.user.RoleOptions;



@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String generateToken(JwtInfo info) {
        long currentTime = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("userId", info.getUserId());
		claims.put("roles", info.getRoles());

        return Jwts
            .builder()
            .claims(claims)
            .subject(info.getUsername())
            .issuedAt(new Date(currentTime))
            .expiration(new Date(currentTime + getExpirationTime()))
            .signWith(getSignInKey())
            .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
            .parser()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getPayload();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtInfo getInfo(String token) {

		Claims claims = extractAllClaims(token);
        List<String> roles = (List<String>) claims.get("roles");

		return JwtInfo.builder()
            .userId(Long.valueOf((Integer) claims.get("userId")))
            .username(claims.getSubject())
            .roles(roles.stream()
                .map( role -> 
                    RoleOptions.valueOf(role)
                )
                .collect(Collectors.toList())
            )
            .build();

	}
    
}
