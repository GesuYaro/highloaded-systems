package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.university.database.GroupsRepository;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import com.dedlam.ftesterlab.domain.university.database.SubjectRepository;
import com.dedlam.ftesterlab.domain.university.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

@Service
public class GroupsServiceImpl implements GroupsService {
  private final Logger logger;
  private final StudentsInfoRepository studentsInfoRepository;
  private final GroupsRepository repository;
  private final TransactionTemplate transactionTemplate;
  private final SubjectRepository subjectRepository;

  public GroupsServiceImpl(
    StudentsInfoRepository studentsInfoRepository,
    GroupsRepository repository,
    TransactionTemplate transactionTemplate,
    SubjectRepository subjectRepository,
    Logger logger
  ) {
    this.studentsInfoRepository = studentsInfoRepository;
    this.repository = repository;
    this.transactionTemplate = transactionTemplate;
    this.subjectRepository = subjectRepository;
    this.logger = logger;
  }

  @Autowired
  public GroupsServiceImpl(
    StudentsInfoRepository studentsInfoRepository,
    GroupsRepository repository,
    TransactionTemplate transactionTemplate,
    SubjectRepository subjectRepository
  ) {
    this(studentsInfoRepository, repository, transactionTemplate, subjectRepository, LoggerFactory.getLogger(GroupsService.class));
  }

  @Override
  public boolean createGroup(String name, int grade, Set<String> subjectNames) {
    var subjects = subjectRepository.findAllByNameIn(subjectNames);
    var group = new Group(null, name, grade, List.of(), subjects);

    try {
      repository.save(group);
      return true;
    } catch (DataAccessException e) {
      return false;
    }
  }

  @Override
  public boolean bindStudentsToGroup(String groupName, Set<UUID> studentsInfoIds) {
    return TRUE.equals(transactionTemplate.execute((ctx) -> {
      var groupOpt = repository.findByName(groupName);
      if (groupOpt.isEmpty()) {
        var msg = String.format("Can't find group with name='%s'", groupName);
        logger.warn(msg);
        return false;
      }

      var group = groupOpt.get();
      var studentsInfo = studentsInfoRepository.findAllByIdIn(studentsInfoIds);

      group.getStudents().addAll(studentsInfo);
      studentsInfo.forEach(info -> info.setGroup(group));

      try {
        repository.save(group);
      } catch (DataAccessException e) {
        logger.error("Can't save group", e);
        ctx.setRollbackOnly();
        return false;
      }

      return true;
    }));
  }

  @Override
  public Window<Group> groups(OffsetScrollPosition offsetScrollPosition) {
    return repository.findByGradeNotNullOrderByNameAsc(offsetScrollPosition);
  }
}
