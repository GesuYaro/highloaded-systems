package com.dedlam.ftesterlab.domain.university;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import com.dedlam.ftesterlab.domain.university.models.StudentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Service
public class StudentsService {
  private static final Logger logger = LoggerFactory.getLogger(StudentsService.class);

  private final StudentsInfoRepository repository;
  private final GroupsService groupsService;
  private final TransactionTemplate transactionTemplate;

  public StudentsService(StudentsInfoRepository repository, GroupsService groupsService, TransactionTemplate transactionTemplate) {
    this.repository = repository;
    this.groupsService = groupsService;
    this.transactionTemplate = transactionTemplate;
  }


  public boolean createAndInitStudentsInfo(String groupName, List<Person> people) {
    var studentsInfo = people
      .stream()
      .map(person -> new StudentInfo(null, person, null))
      .toList();

    Boolean success = transactionTemplate.execute(ctx -> {
      List<StudentInfo> savedStudents;
      try {
        savedStudents = repository.saveAll(studentsInfo);
      } catch (RuntimeException e) {
        logger.error("Can't create students -> ROLLBACK");
        ctx.setRollbackOnly();
        return false;
      }

      var studentIds = savedStudents.stream().map(StudentInfo::getId).collect(Collectors.toSet());
      boolean bindToGroupSuccess = groupsService.bindStudentsToGroup(groupName, studentIds);

      if (!bindToGroupSuccess) {
        logger.error("Can't bind students to group");
        ctx.setRollbackOnly();
        return false;
      }

      return true;
    });

    return TRUE.equals(success);
  }
}
