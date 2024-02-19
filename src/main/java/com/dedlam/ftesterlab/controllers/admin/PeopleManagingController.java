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
  private final TeachersService teachersService;

  public PeopleManagingController(UsersRepository usersRepository, PeopleRepository peopleRepository, TeachersService teachersService) {
    this.usersRepository = usersRepository;
    this.peopleRepository = peopleRepository;
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

  private static PersonView personView(Person person) {
    return new PersonView(
      person.getUser().getUsername(), person.getId().toString(), person.getName(),
      person.getMiddleName(), person.getLastName(), person.getBirthday()
    );
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
