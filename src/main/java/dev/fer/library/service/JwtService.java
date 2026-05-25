package dev.fer.library.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  private String secret;
  private Long expirationMillis;

  public JwtService(
    @Value("${jwt.secret}") String secret, 
    @Value("${jwt.expiration.millis}") long expirationMillis
  ) {
    this.secret = secret;
    this.expirationMillis = expirationMillis;
  }

  public String generateToken(UserDetails user) {
    return Jwts.builder()
      .subject(user.getUsername())
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expirationMillis ))
      .signWith(getSigningKey(), Jwts.SIG.HS256)
      .compact();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);

    return Keys.hmacShaKeyFor(keyBytes);
  } 


  public boolean validateToken(String token) {
    try {
      getAllClaims(token);
      return true;
    } catch(JwtException ex) {
      return false;
    }
  }

  public String extractSubject(String token) {
    return getAllClaims(token).getSubject();
  }

  private Claims getAllClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  } 
}