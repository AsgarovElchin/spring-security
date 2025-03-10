package elchinasgarov.plantly_backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;



@Service
public class JWTService {

    @Value("${jwt.secret.access}")
    private String accessSecretKey;

    @Value("${jwt.secret.refresh}")
    private String refreshSecretKey;


    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60))
                .and()
                .signWith(getAccessKey())
                .compact();
    }


    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .and()
                .signWith(getRefreshKey())
                .compact();
    }


    public String extractUserName(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getSubject, isRefreshToken);
    }


    public boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token, false);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, false);
    }


    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token, true);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, true);
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isRefreshToken) {
        final Claims claims = extractAllClaims(token, isRefreshToken);
        return claimsResolver.apply(claims);
    }


    private Date extractExpiration(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getExpiration, isRefreshToken);
    }


    private boolean isTokenExpired(String token, boolean isRefreshToken) {
        return extractExpiration(token, isRefreshToken).before(new Date());
    }


    private Claims extractAllClaims(String token,boolean isRefreshToken) {
        SecretKey key = isRefreshToken ? getRefreshKey() : getAccessKey();
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getAccessKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private SecretKey getRefreshKey() {
        byte[] keyBytes = Decoders.BASE64.decode(refreshSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
