package com.dedlam.ftesterlab.domain.users;

import com.dedlam.ftesterlab.auth.AuthServiceClient;
import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;
import com.dedlam.ftesterlab.auth.models.User;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import com.dedlam.ftesterlab.utils.exceptions.ExceptionType;
import feign.FeignException;
import jakarta.annotation.Nullable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class RemoteUserService implements UserService {

  private final AuthServiceClient authServiceClient;
  private final CircuitBreaker cb;

  public RemoteUserService(AuthServiceClient authServiceClient, CircuitBreakerFactory cbFactory) {
    this.authServiceClient = authServiceClient;
    this.cb = cbFactory.create("cb");
  }

  public Boolean userExists(UUID id) {
    return authServiceClient.userExists(id);
  }

  public @Nullable User user(UUID id) {
    return doRequestWithCb(() -> {
      try {
        return authServiceClient.user(id);
      } catch (FeignException.NotFound e) {
        return null;
      }
    });
  }

  public Boolean existsByUsername(String username) {
    return doRequestWithCb(() -> authServiceClient.existsByUsername(username));
  }

  public @Nullable User findUserByUsername(String username) {
    return doRequestWithCb(() -> {
      try {
        return authServiceClient.findUserByUsername(username);
      } catch (FeignException.NotFound e) {
        return null;
      }
    });
  }

  public List<User> findUsersByUsernames(Set<String> usernames) {
    return doRequestWithCb(() -> authServiceClient.findUsersByUsernames(usernames));
  }

  public @Nullable UUID createUser(CreateUserRequest createUserRequest) {
    return doRequestWithCb(() -> {
      try {
        return authServiceClient.createUser(createUserRequest);
      } catch (FeignException e) {
        return null;
      }
    });
  }

  private <T> T doRequestWithCb(Supplier<T> supplier) {
    return cb.run(supplier, throwable -> {
      throw new BaseException(ExceptionType.BAD_REQUEST);
    });
  }
}
