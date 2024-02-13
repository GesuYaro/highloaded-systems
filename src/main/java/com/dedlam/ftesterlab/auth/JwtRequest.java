package com.dedlam.ftesterlab.auth;

public record JwtRequest(
  String login,
  String password
) {
}
