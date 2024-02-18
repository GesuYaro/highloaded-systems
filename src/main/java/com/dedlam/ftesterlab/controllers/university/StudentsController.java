package com.dedlam.ftesterlab.controllers.university;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/students")
public class StudentsController extends BaseController {
  private static final Logger logger = LoggerFactory.getLogger(StudentsController.class);

  private final StudentsInfoRepository studentsInfoRepository;

  public StudentsController(UsersRepository usersRepository, PeopleService peopleService, StudentsInfoRepository studentsInfoRepository) {
    super(usersRepository, peopleService);
    this.studentsInfoRepository = studentsInfoRepository;
  }

  @GetMapping("/info")
  public ResponseEntity<?> studentInfo() {
    var person = person();
    if (person == null) {
      var user = user();
      var msg = String.format(
        "Can't receive student info, because no person-info for user '%s' with id='%s'", user.getUsername(), user.getId()
      );
      logger.warn(msg);
      return new ResponseEntity<>(msg, UNPROCESSABLE_ENTITY);
    }

    var studentInfoOpt = studentsInfoRepository.findStudentInfoByStudent_Id(person.getId());
    if (studentInfoOpt.isEmpty()) {
      var user = user();
      var msg = String.format(
        "Can't receive student info, because student-info is not initialized for student '%s'", user.getUsername()
      );
      logger.warn(msg);
      return new ResponseEntity<>(msg, UNPROCESSABLE_ENTITY);
    }
    var studentInfo = studentInfoOpt.get();
    var group = studentInfo.getGroup();
    var view = new StudentInfoView(group.getName(), group.getGrade());
    return ResponseEntity.ok(view);
  }

  public record StudentInfoView(
    String groupName,
    int grade
  ) {
  }
}
