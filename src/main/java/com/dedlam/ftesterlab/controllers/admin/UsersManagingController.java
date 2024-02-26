package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.AuthServiceImpl;
import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dedlam.ftesterlab.auth.dto.CreateUserRequest.UserType.*;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UsersManagingController {
  private static final Logger logger = LoggerFactory.getLogger(UsersManagingController.class);

  private final AuthServiceImpl authService;

  @PostMapping("/registration/students")
  public ResponseEntity<?> registerStudent(@RequestBody RegisterUserRequest request) {
    String username = request.username;

    if (authService.existsByUsername(request.username)) {
      var message = String.format("User with username '%s' is already exists. Failed to register user", username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var studentRequest = new CreateUserRequest(STUDENT, username, request.password);
    var savedStudentId = authService.createUser(studentRequest);

    return ResponseEntity.ok(savedStudentId);
  }

  @PostMapping("/registration/teachers")
  public ResponseEntity<?> registerTeacher(@RequestBody RegisterUserRequest request) {
    String username = request.username;

    if (authService.existsByUsername(request.username)) {
      var message = String.format("User with username '%s' is already exists. Failed to register user", username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var teacher = new CreateUserRequest(TEACHER, username, request.password);
    var savedTeacherId = authService.createUser(teacher);

    return ResponseEntity.ok(savedTeacherId);
  }

  public record RegisterUserRequest(String username, String password) {
  }
}
