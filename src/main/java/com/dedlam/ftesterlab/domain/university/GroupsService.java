package com.dedlam.ftesterlab.domain.university;

import com.dedlam.ftesterlab.domain.university.database.GroupsRepository;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Set;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

@Service
public class GroupsService {
  private static final Logger logger = LoggerFactory.getLogger(GroupsService.class);
  private final StudentsInfoRepository studentsInfoRepository;
  private final GroupsRepository groupsRepository;
  private final TransactionTemplate transactionTemplate;

  public GroupsService(StudentsInfoRepository studentsInfoRepository, GroupsRepository groupsRepository, TransactionTemplate transactionTemplate) {
    this.studentsInfoRepository = studentsInfoRepository;
    this.groupsRepository = groupsRepository;
    this.transactionTemplate = transactionTemplate;
  }

  public boolean createGroup(String name, int grade, Set<String> subjectNames) {
    return true;
  }

  public boolean bindStudentsToGroup(String groupName, Set<UUID> studentsInfoIds) {
    return TRUE.equals(transactionTemplate.execute((ctx) -> {
      var group = groupsRepository.findByName(groupName).orElseThrow(() -> new RuntimeException("TODO"));
      var studentsInfo = studentsInfoRepository.findAllByIdIn(studentsInfoIds);

      group.setStudents(studentsInfo);

      try {
        groupsRepository.save(group);
      } catch (RuntimeException e) {
        logger.error("Can't save group");
        ctx.setRollbackOnly();
        return false;
      }

      return true;
    }));
  }
}
