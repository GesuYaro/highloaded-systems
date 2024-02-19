package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.models.DefaultUser;
import com.dedlam.ftesterlab.auth.models.Student;
import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.university.database.GroupsRepository;
import com.dedlam.ftesterlab.domain.university.services.StudentsService;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("admin/students")
public class StudentsManagingController {
  private static final Logger logger = LoggerFactory.getLogger(StudentsManagingController.class);

  private final PeopleRepository peopleRepository;
  private final StudentsService studentsService;
  private final GroupsRepository groupsRepository;

  public StudentsManagingController(PeopleRepository peopleRepository, StudentsService studentsService, GroupsRepository groupsRepository) {
    this.peopleRepository = peopleRepository;
    this.studentsService = studentsService;
    this.groupsRepository = groupsRepository;
  }

  @PostMapping("init")
  public ResponseEntity<?> initStudentsInfo(@RequestBody InitStudentsInfoRequest request) {
    Set<String> logins = Set.copyOf(request.peopleLogins);
    var people = peopleRepository.findAllByUserUsernameIn(logins);

    var errorReason = validateStudentsForInit(people, request);
    if (errorReason != null) {
      var message = "Can't init students, because " + errorReason;
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var result = studentsService.createAndInitStudentsInfo(request.groupName, people);

    return ResponseEntity.ok(result);
  }

  @PutMapping("change-group")
  public ResponseEntity<?> updateStudentsGroup(@RequestBody UpdateStudentsGroupRequest request) {
    var logins = Set.copyOf(request.peopleLogins);
    var people = peopleRepository.findAllByUserUsernameIn(logins);

    var accountsErrorReason = validatePeopleForStudentsAccount(people, logins);
    if (accountsErrorReason != null) {
      var message = "Can't update students group, because " + accountsErrorReason;
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    if (request.newGroupName == null) {
      studentsService.removeFromGroup(people);
    } else {
      studentsService.changeGroup(request.newGroupName, people);
    }

    return ResponseEntity.ok(null);
  }

  private @Nullable String validatePeopleForStudentsAccount(List<Person> people, Set<String> logins) {
    if (people.size() != logins.size()) {
      var foundLogins = people.stream().map(p -> p.getUser().getUsername()).collect(Collectors.toSet());
      var unknownLogins = logins.stream().filter(login -> !foundLogins.contains(login)).collect(Collectors.toSet());
      return String.format("can't find people with usernames %s", unknownLogins);
    }

    var notStudentsUsernames = people.stream()
      .map(Person::getUser)
      .filter(user -> !Student.class.isAssignableFrom(user.getClass()))
      .map(DefaultUser::getUsername)
      .toList();

    if (!notStudentsUsernames.isEmpty()) {
      return String.format("users with usernames %s aren't students", notStudentsUsernames);
    }

    return null;
  }

  private @Nullable String validateStudentsForInit(List<Person> people, InitStudentsInfoRequest request) {
    Set<String> logins = Set.copyOf(request.peopleLogins);

    var wrongAccountsErrorReason = validatePeopleForStudentsAccount(people, logins);
    if (wrongAccountsErrorReason != null) {
      return wrongAccountsErrorReason;
    }

    if (!groupsRepository.existsByName(request.groupName)) {
      return String.format("Can't init students, because group '%s' doesn't exist", request.groupName);
    }

    return null;
  }

  public record InitStudentsInfoRequest(
    String groupName,
    List<String> peopleLogins
  ) {
  }

  public record UpdateStudentsGroupRequest(
    @Nullable String newGroupName,
    List<String> peopleLogins
  ) {
  }
}
