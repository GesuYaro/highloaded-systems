package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import com.dedlam.ftesterlab.domain.university.models.StudentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Service
public class StudentsServiceImpl implements StudentsService {

  private final StudentsInfoRepository repository;
  private final GroupsService groupsService;
  private final TransactionTemplate transactionTemplate;
  private final Logger logger;

  public StudentsServiceImpl(StudentsInfoRepository repository, GroupsService groupsService, TransactionTemplate transactionTemplate, Logger logger) {
    this.repository = repository;
    this.groupsService = groupsService;
    this.transactionTemplate = transactionTemplate;
    this.logger = logger;
  }

  @Autowired
  public StudentsServiceImpl(StudentsInfoRepository repository, GroupsService groupsService, TransactionTemplate transactionTemplate) {
    this(repository, groupsService, transactionTemplate, LoggerFactory.getLogger(StudentsService.class));
  }


  @Override
  public boolean createAndInitStudentsInfo(String groupName, List<Person> people) {
    var studentsInfo = people
      .stream()
      .map(person -> new StudentInfo(null, person.getId(), null))
      .toList();

    Boolean success = transactionTemplate.execute(ctx -> {
      List<StudentInfo> savedStudents;
      try {
        savedStudents = repository.saveAll(studentsInfo);
      } catch (DataAccessException e) {
        logger.error("Can't create students -> ROLLBACK", e);
        ctx.setRollbackOnly();
        return false;
      }

      var studentIds = savedStudents.stream().map(StudentInfo::getId).collect(Collectors.toSet());
      boolean bindToGroupSuccess = groupsService.bindStudentsToGroup(groupName, studentIds);

      if (!bindToGroupSuccess) {
        logger.error("Can't bind students to group -> ROLLBACK");
        ctx.setRollbackOnly();
        return false;
      }

      return true;
    });

    return TRUE.equals(success);
  }

  @Override
  public boolean changeGroup(String newGroupName, List<Person> people) {
    var peopleIds = people.stream().map(Person::getId).collect(Collectors.toSet());
    var studentInfos = repository.findAllByStudentIdIn(peopleIds);
    var studentInfoIds = studentInfos.stream().map(StudentInfo::getId).collect(Collectors.toSet());
    return groupsService.bindStudentsToGroup(newGroupName, studentInfoIds);
  }

  @Override
  public boolean removeFromGroup(List<Person> people) {
    var peopleIds = people.stream().map(Person::getId).collect(Collectors.toSet());
    var studentInfos = repository.findAllByStudentIdIn(peopleIds);

    studentInfos.forEach(info -> info.setGroup(null));

    repository.saveAll(studentInfos);

    return true;
  }
}
