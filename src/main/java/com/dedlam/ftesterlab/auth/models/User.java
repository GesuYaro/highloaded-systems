package com.dedlam.ftesterlab.auth.models;

import java.util.Set;
import java.util.UUID;

public record User(
  UUID id,
  String username,
  Set<String> roles
) {
}
