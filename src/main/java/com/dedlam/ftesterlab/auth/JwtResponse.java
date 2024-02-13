package com.dedlam.ftesterlab.auth;

public record JwtResponse(
  String accessToken,
  String refreshToken
) {
}
