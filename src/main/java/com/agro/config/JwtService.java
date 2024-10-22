package com.agro.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.SignatureException;
import com.agro.public_.tables.records.UserInfosRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Date;
import java.security.Key;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public String extractUserUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolve) {
        final Claims claims = extractAllClaims(token);
        return claimsResolve.apply(claims);
    }

    public String generateToken(UserInfosRecord userInfosRecord) {
        return generateToken(new HashMap<>(), userInfosRecord);
    }

    public String generateToken(
            Map<String, Object> extractClaim,
            UserInfosRecord userInfosRecord
    ) {
        return Jwts.builder().claims(extractClaim).subject(userInfosRecord.getUsername())
                .issuedAt(new Date(System.currentTimeMillis())).expiration(
                        new Date(System.currentTimeMillis() + 10000 * 60 * 24)
                )
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserUsername(token);

        if (isTokenExpired(token))
            throw new MalformedJwtException("JWT Token is expired!");

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new MalformedJwtException("JWT Token is not valid!");
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}