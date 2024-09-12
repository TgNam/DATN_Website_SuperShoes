package org.example.datn_website_supershoes.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails);
    }

    public String createToken(HashMap<String, Objects> map, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(map)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extract(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String email = extract(token);
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        if (expirationDate.before(new Date())) return false;
        return email.equals(userDetails.getUsername());
    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractALlClaim(token);
        return claimsTFunction.apply(claims);
    }

    public Claims extractALlClaim(String token) {
        return Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }
}
