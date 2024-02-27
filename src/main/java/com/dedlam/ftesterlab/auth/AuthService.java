package com.dedlam.ftesterlab.auth;

import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;
import com.dedlam.ftesterlab.feign.AuthServiceClient;
import com.dedlam.ftesterlab.feign.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthServiceClient authServiceClient;

  public Boolean userExists(UUID id) {
    return authServiceClient.userExists(id);
  }

  public User user(UUID id) {
    return authServiceClient.user(id);
  }

  public Boolean existsByUsername(String username) {
    return authServiceClient.existsByUsername(username);
  }

  public User findUserByUsername(String username) {
    return authServiceClient.findUserByUsername(username);
  }

  public List<User> findUsersByUsernames(Set<String> usernames) {
    return authServiceClient.findUsersByUsernames(usernames);
  }

  public UUID createUser(CreateUserRequest createUserRequest) {
    return authServiceClient.createUser(createUserRequest);
  }
}
