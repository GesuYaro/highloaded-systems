package com.dedlam.ftesterlab.auth;

import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;
import com.dedlam.ftesterlab.feign.AuthServiceClient;
import com.dedlam.ftesterlab.feign.dto.User;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import com.dedlam.ftesterlab.utils.exceptions.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {

  private final AuthServiceClient authServiceClient;
  private final CircuitBreaker cb;

  public AuthService(AuthServiceClient authServiceClient, CircuitBreakerFactory circuitBreakerFactory) {
    this.authServiceClient = authServiceClient;
    this.cb = circuitBreakerFactory.create("cb-auth");
  }

  public Boolean userExists(UUID id) {
    return cb.run(
            () -> authServiceClient.userExists(id),
            throwable -> { throw new BaseException(ExceptionType.BAD_REQUEST); }
    );
  }

  public User user(UUID id) {
    return cb.run(
            () -> authServiceClient.user(id),
            throwable -> { throw new BaseException(ExceptionType.BAD_REQUEST); }
    );
  }

  public Boolean existsByUsername(String username) {
    return cb.run(
            () -> authServiceClient.existsByUsername(username),
            throwable -> { throw new BaseException(ExceptionType.BAD_REQUEST); }
    );
  }

  public User findUserByUsername(String username) {
    return cb.run(
            () -> authServiceClient.findUserByUsername(username),
            throwable -> { throw new BaseException(ExceptionType.BAD_REQUEST); }
    );
  }

  public List<User> findUsersByUsernames(Set<String> usernames) {
    return cb.run(
            () -> authServiceClient.findUsersByUsernames(usernames),
            throwable -> { throw new BaseException(ExceptionType.BAD_REQUEST); }
    );
  }

  public UUID createUser(CreateUserRequest createUserRequest) {
    return cb.run(
            () -> authServiceClient.createUser(createUserRequest),
            throwable -> { throw new BaseException(ExceptionType.BAD_REQUEST); }
    );
  }
}
