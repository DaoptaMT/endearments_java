package mt.endearments.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import mt.endearments.enums.TokenType;
import mt.endearments.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.refresh-key}")
    private String refreshKey;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Value("${jwt.reset-expiration:900000}")
    private long resetExpiration;

    @Override
    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, accessExpiration, TokenType.ACCESS_TOKEN);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration, TokenType.REFRESH_TOKEN);
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {
        return extractClaim(token, Claims::getSubject, tokenType);
    }

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType) {
        final String username = extractUsername(token, tokenType);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType));
    }

    // ==================== Reset Token ====================

    @Override
    public String generateResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + resetExpiration))
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateResetToken(String token, String email) {
        try {
            String subject = Jwts.parserBuilder()
                    .setSigningKey(getKey(TokenType.ACCESS_TOKEN))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return subject.equals(email) && !isResetTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isResetTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getKey(TokenType.ACCESS_TOKEN))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    // ==================== Private Helpers ====================
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, TokenType tokenType) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType tokenType) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(tokenType))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    private Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, Claims::getExpiration, tokenType);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long expiration, TokenType tokenType) {
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(tokenType), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType tokenType) {
        byte[] keyBytes;
        if (tokenType.equals(TokenType.ACCESS_TOKEN)) {
            keyBytes = Decoders.BASE64.decode(secretKey);
        } else {
            keyBytes = Decoders.BASE64.decode(refreshKey);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
