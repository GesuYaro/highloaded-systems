package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.AuthService;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.university.services.TeachersService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TeachersManagingController {
  private static final Logger logger = LoggerFactory.getLogger(TeachersManagingController.class);
  private final AuthService authService;
  private final PeopleService peopleService;
  private final TeachersService teachersService;

  @PostMapping("init")
  public ResponseEntity<?> initTeachersInfo(@RequestBody InitTeachersInfoRequest request) {
    var teacherUser = authService.findUserByUsername(request.username);
    if (teacherUser == null) {
      var message = String.format("Can't init teacher, because can't find teacher with username '%s'", request.username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var teacher = peopleService.personByUserId(teacherUser.id());
    if (teacher == null) {
      var message = String.format("Can't init teacher, because can't find person with username '%s'", request.username);
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    boolean result = teachersService.createAndInitTeachersInfo(teacher);

    return ResponseEntity.ok(result);
  }

  public record InitTeachersInfoRequest(
    String username
  ) {
  }
}
