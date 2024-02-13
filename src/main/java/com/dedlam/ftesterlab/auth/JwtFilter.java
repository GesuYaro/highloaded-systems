package com.dedlam.ftesterlab.auth;

import com.dedlam.ftesterlab.auth.models.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends GenericFilterBean {


  private static final String AUTHORIZATION = "Authorization";

  private final JwtProvider jwtProvider;

  public JwtFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
    final String token = getTokenFromRequest((HttpServletRequest) request);
    if (token != null && jwtProvider.validateAccessToken(token)) {
      Claims claims = jwtProvider.getAccessClaims(token);
      String username = claims.getSubject();
      var roles =((List<String>) claims.get("roles")).stream().map(Role::valueOf).collect(Collectors.toSet());
      JwtAuthentication jwtInfoToken = new JwtAuthentication(username, roles, true);
      SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
    }
    fc.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    final String bearer = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }

}