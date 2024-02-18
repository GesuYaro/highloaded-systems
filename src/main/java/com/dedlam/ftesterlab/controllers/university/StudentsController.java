package com.dedlam.ftesterlab.controllers.university;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import com.dedlam.ftesterlab.utils.exceptions.MyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentsController extends BaseController {
  private final StudentsInfoRepository studentsInfoRepository;

  public StudentsController(UsersRepository usersRepository, PeopleService peopleService, StudentsInfoRepository studentsInfoRepository) {
    super(usersRepository, peopleService);
    this.studentsInfoRepository = studentsInfoRepository;
  }

  @GetMapping("/info")
  public ResponseEntity<StudentInfoView> studentInfo() {
    var person = person();
    var studentInfo = studentsInfoRepository.findStudentInfoByStudent_Id(person.getId()).orElseThrow(() -> new MyException("Can't find student info"));
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
