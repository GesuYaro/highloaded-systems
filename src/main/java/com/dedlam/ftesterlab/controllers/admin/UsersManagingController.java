package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.auth.models.Student;
import com.dedlam.ftesterlab.auth.models.Teacher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("admin")
public class UsersManagingController {
  private final UsersRepository usersRepository;

  public UsersManagingController(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  @PostMapping("registration/students")
  public ResponseEntity<UUID> registerStudent(@RequestBody RegisterUserRequest request) {
    var student = new Student(null, request.username, request.password);
    var savedStudent = usersRepository.save(student);

    return ResponseEntity.ok(savedStudent.getId());
  }

  @PostMapping("registration/teachers")
  public ResponseEntity<UUID> registerTeacher(@RequestBody RegisterUserRequest request) {
    var teacher = new Teacher(null, request.username, request.password);
    var savedTeacher = usersRepository.save(teacher);

    return ResponseEntity.ok(savedTeacher.getId());
  }

  public record RegisterUserRequest(String username, String password) {
  }
}
