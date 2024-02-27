package com.dedlam.ftesterlab.domain.users;

import com.dedlam.ftesterlab.auth.AuthServiceClient;
import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;
import com.dedlam.ftesterlab.auth.models.User;
import feign.FeignException;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoteUserService implements UserService {

  private final AuthServiceClient authServiceClient;

  public Boolean userExists(UUID id) {
    return authServiceClient.userExists(id);
  }

  public @Nullable User user(UUID id) {
    try {
      return authServiceClient.user(id);
    } catch (FeignException.NotFound e) {
      return null;
    }
  }

  public Boolean existsByUsername(String username) {
    return authServiceClient.existsByUsername(username);
  }

  public @Nullable User findUserByUsername(String username) {
    try {
      return authServiceClient.findUserByUsername(username);
    } catch (FeignException.NotFound e) {
      return null;
    }
  }

  public List<User> findUsersByUsernames(Set<String> usernames) {
    return authServiceClient.findUsersByUsernames(usernames);
  }

  public @Nullable UUID createUser(CreateUserRequest createUserRequest) {
    try {
      return authServiceClient.createUser(createUserRequest);
    } catch (FeignException e) {
      return null;
    }
  }
}
