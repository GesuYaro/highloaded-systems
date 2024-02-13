package com.dedlam.ftesterlab.auth;

import jakarta.security.auth.message.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("login")
  public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
    JwtResponse token = authService.login(authRequest);
    return ResponseEntity.ok(token);
  }

  @PostMapping("token")
  public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
    JwtResponse token = authService.getAccessToken(request.refreshToken());
    return ResponseEntity.ok(token);
  }

  @PostMapping("refresh")
  public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
    JwtResponse token = authService.refresh(request.refreshToken());
    return ResponseEntity.ok(token);
  }
}
