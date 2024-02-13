package com.dedlam.ftesterlab.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {
  private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

  private final SecretKey jwtAccessSecret;
  private final SecretKey jwtRefreshSecret;

  public JwtProvider(
    @Value("${jwt.secret.access}") String jwtAccessSecret,
    @Value("${jwt.secret.refresh}") String jwtRefreshSecret
  ) {
    this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
  }

  public String generateAccessToken(@Nonnull UserDetails userDetails) {
    var now = LocalDateTime.now();
    var accessExpirationInstant = now.plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant();
    var expiredAt = Date.from(accessExpirationInstant);

    return Jwts.builder()
      .subject(userDetails.getUsername())
      .expiration(expiredAt)
      .signWith(jwtAccessSecret)
      .claim("roles", userDetails.getAuthorities())
      .compact();
  }

  public String generateRefreshToken(@Nonnull UserDetails userDetails) {
    var now = LocalDateTime.now();
    var refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
    var refreshExpiredAt = Date.from(refreshExpirationInstant);
    return Jwts.builder()
      .subject(userDetails.getUsername())
      .expiration(refreshExpiredAt)
      .signWith(jwtRefreshSecret)
      .compact();
  }

  public boolean validateAccessToken(@Nonnull String accessToken) {
    return validateToken(accessToken, jwtAccessSecret);
  }

  public boolean validateRefreshToken(@Nonnull String refreshToken) {
    return validateToken(refreshToken, jwtRefreshSecret);
  }

  private boolean validateToken(@Nonnull String token, @Nonnull SecretKey secret) {
    try {
      Jwts.parser()
        .verifyWith(secret)
        .build()
        .parse(token);
      return true;
    } catch (ExpiredJwtException expEx) {
      logger.error("Token expired", expEx);
    } catch (UnsupportedJwtException unsEx) {
      logger.error("Unsupported jwt", unsEx);
    } catch (MalformedJwtException mjEx) {
      logger.error("Malformed jwt", mjEx);
    } catch (SignatureException sEx) {
      logger.error("Invalid signature", sEx);
    } catch (Exception e) {
      logger.error("invalid token", e);
    }
    return false;
  }

  public Claims getAccessClaims(@Nonnull String token) {
    return getClaims(token, jwtAccessSecret);
  }

  public Claims getRefreshClaims(@Nonnull String token) {
    return getClaims(token, jwtRefreshSecret);
  }

  private Claims getClaims(@Nonnull String token, @Nonnull SecretKey secret) {
    return Jwts.parser()
      .verifyWith(secret)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}
