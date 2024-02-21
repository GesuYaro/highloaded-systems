package com.dedlam.ftesterlab.controllers.university;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.tests.mappers.DeadlineMapper;
import com.dedlam.ftesterlab.domain.tests.mappers.TestMapper;
import com.dedlam.ftesterlab.domain.tests.services.StudentTestService;
import com.dedlam.ftesterlab.domain.tests.services.dto.*;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/students")
public class StudentsController extends BaseController {
  private static final Logger logger = LoggerFactory.getLogger(StudentsController.class);

  private final StudentsInfoRepository studentsInfoRepository;
  private final StudentTestService studentTestService;
  private final DeadlineMapper deadlineMapper;
  private final TestMapper testMapper;

  public StudentsController(UsersRepository usersRepository, PeopleService peopleService, StudentsInfoRepository studentsInfoRepository, StudentTestService studentTestService, DeadlineMapper deadlineMapper, TestMapper testMapper) {
    super(usersRepository, peopleService);
    this.studentsInfoRepository = studentsInfoRepository;
    this.studentTestService = studentTestService;
    this.deadlineMapper = deadlineMapper;
    this.testMapper = testMapper;
  }

  @GetMapping("/deadlines")
  public Page<DeadlineView> incomingDeadlines(Pageable pageable) {
    var user = person();
    return studentTestService.incomingDeadlines(user, pageable).map(deadlineMapper::toDeadlineView);
  }

  @PostMapping("/tests")
  public TestWithTestResultView startTest(@RequestBody @Validated StartTestDto startTestDto) {
    var user = person();
    return testMapper.toTestWithTestResultView(studentTestService.startTest(startTestDto, user));
  }

  @PostMapping("/tests/submit")
  public TestResultView submitTest(@RequestBody @Validated TestSubmitDto testSubmitDto) {
    var user = person();
    return testMapper.toTestResultView(studentTestService.submitTest(testSubmitDto, user));
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
    var view = group != null
      ? new StudentInfoView(group.getName(), group.getGrade())
      : null;

    return view == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(view);
  }

  public record StudentInfoView(
    String groupName,
    int grade
  ) {
  }
}
