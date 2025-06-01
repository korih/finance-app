package io.github.korih.finance_processor.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${auth.token}")
  private String jwtSecret;

  @Value("${auth.expiration}")
  private long jwtExpiration;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaim(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails user) {
    return generateToken(new HashMap<>(), user);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
    return buildToken(extraClaims, user, jwtExpiration);
  }

  public long getExpiration() {
    return jwtExpiration;
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String userName = extractUsername(token);
    return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  public boolean isTokenValid(String token) {
    return !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private String buildToken(Map<String, Object> extraClaims, UserDetails user, long jwtExpiration2) {
    return Jwts.builder()
    .setClaims(extraClaims)
    .setSubject(user.getUsername())
    .setIssuedAt(new Date(System.currentTimeMillis()))
    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration2))
    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
    .compact();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims extractAllClaim(String token) {
    return Jwts
    .parserBuilder()
    .setSigningKey(getSignInKey())
    .build()
    .parseClaimsJws(token)
    .getBody();
  }
  
}
