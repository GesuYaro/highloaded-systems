package com.dedlam.ftesterlab.domain.users;

import com.dedlam.ftesterlab.auth.models.User;
import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
  Boolean userExists(UUID id);

  @Nullable User user(UUID id);

  Boolean existsByUsername(String username);

  @Nullable User findUserByUsername(String username);

  List<User> findUsersByUsernames(Set<String> usernames);

  @Nullable UUID createUser(CreateUserRequest createUserRequest);
}
