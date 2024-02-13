package com.dedlam.ftesterlab.auth;

import com.dedlam.ftesterlab.auth.database.AdminsRepository;
import com.dedlam.ftesterlab.auth.database.UsersRepository;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
  private final Map<String, String> refreshStorage = new HashMap<>();
  private final AdminsRepository adminsRepository;
  private final UsersRepository usersRepository;
  private final JwtProvider jwtProvider;

  public AuthService(AdminsRepository adminsRepository, UsersRepository usersRepository, JwtProvider jwtProvider) {
    this.adminsRepository = adminsRepository;
    this.usersRepository = usersRepository;
    this.jwtProvider = jwtProvider;
  }

  public JwtResponse login(@Nonnull JwtRequest request) throws AuthException {
    var userDetails = getUserDetailsOrThrow(request.login());
    if (userDetails.getPassword().equals(request.password())) {
      String accessToken = jwtProvider.generateAccessToken(userDetails);
      String refreshToken = jwtProvider.generateRefreshToken(userDetails);
      refreshStorage.put(userDetails.getUsername(), refreshToken);
      return new JwtResponse(accessToken, refreshToken);
    } else {
      throw new AuthException("Invalid password");
    }
  }

  public JwtResponse getAccessToken(@Nonnull String refreshToken) throws AuthException {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      String login = claims.getSubject();
      String saveRefreshToken = refreshStorage.get(login);
      if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
        var userDetails = getUserDetailsOrThrow(login);
        final String accessToken = jwtProvider.generateAccessToken(userDetails);
        return new JwtResponse(accessToken, null);
      }
    }
    return new JwtResponse(null, null);
  }

  public JwtResponse refresh(@Nonnull String refreshToken) throws AuthException {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      String login = claims.getSubject();
      String saveRefreshToken = refreshStorage.get(login);
      if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
        var userDetails = getUserDetailsOrThrow(login);
        final String accessToken = jwtProvider.generateAccessToken(userDetails);
        final String newRefreshToken = jwtProvider.generateRefreshToken(userDetails);
        refreshStorage.put(userDetails.getUsername(), newRefreshToken);
        return new JwtResponse(accessToken, newRefreshToken);
      }
    }
    throw new AuthException("Invalid JWT token");
  }

  public JwtAuthentication getAuthInfo() {
    return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
  }


  private UserDetails getUserDetailsOrThrow(String login) throws AuthException {
    Optional<? extends UserDetails> userDetailsOpt = adminsRepository.findAdminByUsername(login);
    if (userDetailsOpt.isEmpty()) {
      userDetailsOpt = usersRepository.findUserByUsername(login);
    }
    if (userDetailsOpt.isEmpty())
      throw new AuthException("Can't find user");

    return userDetailsOpt.get();
  }
}
