package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.auth.models.Teacher;
import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.university.services.StudentsService;
import com.dedlam.ftesterlab.domain.university.services.TeachersService;
import com.dedlam.ftesterlab.utils.exceptions.MyException;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("admin")
public class PeopleManagingController {
  private final UsersRepository usersRepository;
  private final PeopleRepository peopleRepository;
  private final StudentsService studentsService;
  private final TeachersService teachersService;

  public PeopleManagingController(UsersRepository usersRepository, PeopleRepository peopleRepository, StudentsService studentsService, TeachersService teachersService) {
    this.usersRepository = usersRepository;
    this.peopleRepository = peopleRepository;
    this.studentsService = studentsService;
    this.teachersService = teachersService;
  }

  @GetMapping("people")
  public ResponseEntity<PeopleView> people(@RequestParam int pageNumber) {
    var pageRequest = PageRequest.of(pageNumber, 50);
    var result = peopleRepository.findAll(pageRequest);
    var people = new PeopleView(
      result.getTotalPages(),
      result.toList().stream().map(PeopleManagingController::personView).toList()
    );

    return ResponseEntity.ok(people);
  }

  @PostMapping("students/init")
  public ResponseEntity<Boolean> initStudentsInfo(@RequestBody InitStudentsInfoRequest request) {
    var people = peopleRepository.findAllByUserUsernameIn(Set.copyOf(request.peopleLogins));

    var result = studentsService.createAndInitStudentsInfo(request.groupName, people);

    return ResponseEntity.ok(result);
  }

  @PostMapping("teachers/init")
  public ResponseEntity<Boolean> initTeachersInfo(@RequestBody InitTeachersInfoRequest request) {
    var userDetails = (Teacher) usersRepository.findUserByUsername(request.username).orElseThrow(() -> new MyException("Can't find teacher"));
    var teacher = peopleRepository.findByUserId(userDetails.getId()).orElseThrow(() -> new MyException("Can't find teacher"));

    boolean result = teachersService.createAndInitTeachersInfo(teacher);

    return ResponseEntity.ok(result);
  }

  public record InitStudentsInfoRequest(
    String groupName,
    List<String> peopleLogins
  ) {
  }

  public record InitTeachersInfoRequest(
    String username
  ) {
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
}
