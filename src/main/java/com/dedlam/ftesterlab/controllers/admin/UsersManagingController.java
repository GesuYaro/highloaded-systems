package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;
import com.dedlam.ftesterlab.auth.dto.CreateUserRequest.UserType;
import com.dedlam.ftesterlab.domain.users.UserService;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dedlam.ftesterlab.auth.dto.CreateUserRequest.UserType.STUDENT;
import static com.dedlam.ftesterlab.auth.dto.CreateUserRequest.UserType.TEACHER;
import static com.dedlam.ftesterlab.utils.exceptions.ExceptionType.BAD_REQUEST;

@RestController
@RequestMapping("/admin")
public class UsersManagingController {
  private static final Logger logger = LoggerFactory.getLogger(UsersManagingController.class);

  private final UserService userService;

  public UsersManagingController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/registration/students")
  public ResponseEntity<?> registerStudent(@RequestBody RegisterUserRequest request) {
    try {
      var id = registerUser(STUDENT, request.username, request.password);
      return ResponseEntity.ok(id);
    } catch (BaseException e) {
      return e.getExceptionType() == BAD_REQUEST
        ? ResponseEntity.badRequest().build()
        : ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping("/registration/teachers")
  public ResponseEntity<?> registerTeacher(@RequestBody RegisterUserRequest request) {
    try {
      var id = registerUser(TEACHER, request.username, request.password);
      return ResponseEntity.ok(id);
    } catch (BaseException e) {
      return e.getExceptionType() == BAD_REQUEST
        ? ResponseEntity.badRequest().build()
        : ResponseEntity.internalServerError().build();
    }
  }

  private UUID registerUser(UserType userType, String username, String password) {
    if (userService.existsByUsername(username)) {
      var message = String.format("User with username '%s' is already exists. Failed to register user", username);
      logger.warn(message);
      throw new BaseException(message, BAD_REQUEST);
    }

    var teacher = new CreateUserRequest(userType, username, password);

    return userService.createUser(teacher);
  }

  public record RegisterUserRequest(String username, String password) {
  }
}
