package com.dedlam.ftesterlab.domain.users;

import com.dedlam.ftesterlab.auth.models.User;
import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
  Boolean userExists(UUID id);

  User user(UUID id);

  Boolean existsByUsername(String username);

  User findUserByUsername(String username);

  List<User> findUsersByUsernames(Set<String> usernames);

  UUID createUser(CreateUserRequest createUserRequest);
}
