package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.models.User;
import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.university.database.GroupsRepository;
import com.dedlam.ftesterlab.domain.university.services.StudentsService;
import com.dedlam.ftesterlab.domain.users.UserService;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("admin/students")
public class StudentsManagingController {
  private static final Logger logger = LoggerFactory.getLogger(StudentsManagingController.class);
  private final PeopleService peopleService;
  private final StudentsService studentsService;
  private final GroupsRepository groupsRepository;
  private final UserService userService;

  public StudentsManagingController(PeopleService peopleService, StudentsService studentsService, GroupsRepository groupsRepository, UserService userService) {
    this.peopleService = peopleService;
    this.studentsService = studentsService;
    this.groupsRepository = groupsRepository;
    this.userService = userService;
  }

  @PostMapping("init")
  public ResponseEntity<?> initStudentsInfo(@RequestBody InitStudentsInfoRequest request) {
    Set<String> logins = Set.copyOf(request.peopleLogins);
    var userIds = userService.findUsersByUsernames(logins).stream().map(User::id).collect(Collectors.toSet());

    var people = userIds.stream().map(peopleService::personByUserId).toList();

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
    var userIds = userService.findUsersByUsernames(logins).stream().map(User::id).collect(Collectors.toSet());
    var people = userIds.stream().map(peopleService::personByUserId).toList();

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
    var users = people.stream().map(Person::getUserId).map(userService::user).filter(Objects::nonNull).toList();

    if (people.size() != logins.size()) {
      var foundLogins = users.stream().map(User::username).collect(Collectors.toSet());
      var unknownLogins = logins.stream().filter(login -> !foundLogins.contains(login)).collect(Collectors.toSet());
      return String.format("can't find people with usernames %s", unknownLogins);
    }

    var notStudentsUsernames = users.stream()
      .filter(user -> !user.roles().contains("STUDENT"))
      .map(User::username)
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
