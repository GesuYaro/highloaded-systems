package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.auth.models.Teacher;
import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.university.services.TeachersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("admin/teachers")
public class TeachersManagingController {
  private static final Logger logger = LoggerFactory.getLogger(TeachersManagingController.class);
  private final UsersRepository usersRepository;
  private final PeopleRepository peopleRepository;
  private final TeachersService teachersService;

  public TeachersManagingController(UsersRepository usersRepository, PeopleRepository peopleRepository, TeachersService teachersService) {
    this.usersRepository = usersRepository;
    this.peopleRepository = peopleRepository;
    this.teachersService = teachersService;
  }

  @PostMapping("init")
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

  public record InitTeachersInfoRequest(
    String username
  ) {
  }
}
