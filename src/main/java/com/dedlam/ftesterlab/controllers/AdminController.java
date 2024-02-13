package com.dedlam.ftesterlab.controllers;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.auth.models.Student;
import com.dedlam.ftesterlab.auth.models.Teacher;
import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin")
public class AdminController {
  private final UsersRepository usersRepository;

  private final PeopleRepository peopleRepository;

  public AdminController(UsersRepository usersRepository, PeopleRepository peopleRepository) {
    this.usersRepository = usersRepository;
    this.peopleRepository = peopleRepository;
  }

  @PostMapping("registration/students")
  public ResponseEntity<UUID> registerStudent(@RequestBody RegisterUserRequest request) {
    var student = new Student(null, request.username, request.password);
    var savedStudent = usersRepository.save(student);

    return ResponseEntity.ok(savedStudent.getId());
  }

  @PostMapping("registration/teacher")
  public ResponseEntity<UUID> registerTeacher(@RequestBody RegisterUserRequest request) {
    var student = new Teacher(null, request.username, request.password);
    var savedTeacher = usersRepository.save(student);

    return ResponseEntity.ok(savedTeacher.getId());
  }

  @GetMapping("people")
  public ResponseEntity<PeopleView> people(int pageNumber, int pageOffset) {
    var pageRequest = PageRequest.of(pageNumber, pageOffset);
    var result = peopleRepository.findAll(pageRequest);
    var people = new PeopleView(
      result.getTotalPages(),
      result.toList().stream().map(AdminController::personView).toList()
    );

    return ResponseEntity.ok(people);
  }


  private static PersonView personView(Person person) {
    return new PersonView(
      person.getUser().getUsername(), person.getId().toString(), person.getName(),
      person.getMiddleName(), person.getLastName(), person.getBirthday()
    );
  }

  public record PeopleView(
    int totalPagesNumber,
    List<PersonView> people
  ) {
  }

  public record PersonView(
    String username,
    String id,
    String name,
    String middleName,
    String lastName,
    @JsonFormat(pattern = "dd.MM.yyyy") LocalDate birthday
  ) {
  }

  public record RegisterUserRequest(String username, String password) {
  }
}
