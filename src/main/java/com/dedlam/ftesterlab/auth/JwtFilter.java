package com.dedlam.ftesterlab.auth;

import com.dedlam.ftesterlab.auth.dto.InfoFromTokenRequest;
import com.dedlam.ftesterlab.auth.models.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {


  private static final String AUTHORIZATION = "Authorization";

  private final AuthTokenService authTokenService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
    final String token = getTokenFromRequest((HttpServletRequest) request);
    if (token != null) {
      var infoFromAccessToken = authTokenService.infoFromAccessToken(new InfoFromTokenRequest(token));
      String username = infoFromAccessToken.username();
      var roles = infoFromAccessToken.roles();
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