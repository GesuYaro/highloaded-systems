package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.auth.models.Student;
import com.dedlam.ftesterlab.auth.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("admin")
public class UsersManagingController {
  private static final Logger logger = LoggerFactory.getLogger(UsersManagingController.class);

  private final UsersRepository usersRepository;

  public UsersManagingController(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  @PostMapping("registration/students")
  public ResponseEntity<?> registerStudent(@RequestBody RegisterUserRequest request) {
    String username = request.username;

    if (usersRepository.existsByUsername(request.username)) {
      var message = String.format("User with username '%s' is already exists. Failed to register user", username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var student = new Student(null, username, request.password);
    var savedStudent = usersRepository.save(student);

    return ResponseEntity.ok(savedStudent.getId());
  }

  @PostMapping("registration/teachers")
  public ResponseEntity<?> registerTeacher(@RequestBody RegisterUserRequest request) {
    String username = request.username;

    if (usersRepository.existsByUsername(request.username)) {
      var message = String.format("User with username '%s' is already exists. Failed to register user", username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var teacher = new Teacher(null, username, request.password);
    var savedTeacher = usersRepository.save(teacher);

    return ResponseEntity.ok(savedTeacher.getId());
  }

  public record RegisterUserRequest(String username, String password) {
  }
}
