package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.auth.models.DefaultUser;
import com.dedlam.ftesterlab.auth.models.Student;
import com.dedlam.ftesterlab.auth.models.Teacher;
import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.university.database.GroupsRepository;
import com.dedlam.ftesterlab.domain.university.services.StudentsService;
import com.dedlam.ftesterlab.domain.university.services.TeachersService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("admin")
public class PeopleManagingController {
  private static final Logger logger = LoggerFactory.getLogger(PeopleManagingController.class);

  private final UsersRepository usersRepository;
  private final PeopleRepository peopleRepository;
  private final StudentsService studentsService;
  private final TeachersService teachersService;
  private final GroupsRepository groupsRepository;

  public PeopleManagingController(UsersRepository usersRepository, PeopleRepository peopleRepository, StudentsService studentsService, TeachersService teachersService, GroupsRepository groupsRepository) {
    this.usersRepository = usersRepository;
    this.peopleRepository = peopleRepository;
    this.studentsService = studentsService;
    this.teachersService = teachersService;
    this.groupsRepository = groupsRepository;
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
  public ResponseEntity<?> initStudentsInfo(@RequestBody InitStudentsInfoRequest request) {
    Set<String> logins = Set.copyOf(request.peopleLogins);
    var people = peopleRepository.findAllByUserUsernameIn(logins);

    var validationMessage = validateStudentsForInit(people, request);
    if (validationMessage != null) {
      logger.warn(validationMessage);
      return new ResponseEntity<>(validationMessage, UNPROCESSABLE_ENTITY);
    }

    var result = studentsService.createAndInitStudentsInfo(request.groupName, people);

    return ResponseEntity.ok(result);
  }

  @PostMapping("teachers/init")
  public ResponseEntity<?> initTeachersInfo(@RequestBody InitTeachersInfoRequest request) {
    var teacherUserOpt = usersRepository.findUserByUsername(request.username);
    if (teacherUserOpt.isEmpty()) {
      var message = String.format("Can't init teacher, because can't find teacher with username '%s'", request.username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var user = teacherUserOpt.get();
    if (!Teacher.class.isAssignableFrom(user.getClass())) {
      var message = String.format("Can't init teacher, because user with username '%s' isn't teacher", request.username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var teacherOpt = peopleRepository.findByUserId(user.getId());
    if (teacherOpt.isEmpty()) {
      var message = String.format("Can't init teacher, because can't find person with username '%s'", request.username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    boolean result = teachersService.createAndInitTeachersInfo(teacherOpt.get());

    return ResponseEntity.ok(result);
  }

  private @Nullable String validateStudentsForInit(List<Person> people, InitStudentsInfoRequest request) {
    Set<String> logins = Set.copyOf(request.peopleLogins);

    if (people.size() != logins.size()) {
      var foundLogins = people.stream().map(p -> p.getUser().getUsername()).collect(Collectors.toSet());
      var unknownLogins = logins.stream().filter(login -> !foundLogins.contains(login)).collect(Collectors.toSet());
      return String.format("Can't init students, because can't find people with usernames %s", unknownLogins);
    }

    var notStudentsUsernames = people.stream()
      .map(Person::getUser)
      .filter(user -> !Student.class.isAssignableFrom(user.getClass()))
      .map(DefaultUser::getUsername)
      .toList();

    if (!notStudentsUsernames.isEmpty()) {
      return String.format("Can't init students, because users with usernames %s aren't students", notStudentsUsernames);
    }

    if (!groupsRepository.existsByName(request.groupName)) {
      return String.format("Can't init students, because group '%s' doesn't exist", request.groupName);
    }

    return null;
  }

  private static PersonView personView(Person person) {
    return new PersonView(
      person.getUser().getUsername(), person.getId().toString(), person.getName(),
      person.getMiddleName(), person.getLastName(), person.getBirthday()
    );
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
