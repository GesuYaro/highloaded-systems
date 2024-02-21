package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.university.database.GroupsRepository;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import com.dedlam.ftesterlab.domain.university.database.SubjectRepository;
import com.dedlam.ftesterlab.domain.university.models.Group;
import com.dedlam.ftesterlab.domain.university.models.StudentInfo;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GroupsServiceImplTest {
  private final Logger logger = mock(Logger.class);
  private final StudentsInfoRepository studentsInfoRepository = mock();
  private final GroupsRepository repository = mock();
  private final TransactionStatus txStatus = mock();
  private final TransactionTemplate transactionTemplate = mock();
  private final SubjectRepository subjectRepository = mock();
  private final GroupsServiceImpl service =
    new GroupsServiceImpl(studentsInfoRepository, repository, transactionTemplate, subjectRepository, logger);

  @BeforeEach
  public void setUp() {
    when(transactionTemplate.execute(any())).thenAnswer(invocation -> ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(txStatus));
  }

  @AfterEach
  public void checkNoMoreInvocations() {
    verifyNoMoreInteractions(studentsInfoRepository, repository, transactionTemplate, subjectRepository, logger);
  }

  @Test
  public void createGroup() {
    var subjectNames = Set.of("Subject 1", "Subject 2");
    var groupName = "Group";
    var grade = 123;
    var subjects = List.of(
      new Subject(UUID.randomUUID(), "Subject 1", null),
      new Subject(UUID.randomUUID(), "Subject 2", null)
    );
    var expectedGroupToSave = new Group(null, groupName, grade, Collections.emptyList(), subjects);
    var groupToSaveCaptor = ArgumentCaptor.forClass(Group.class);
    when(subjectRepository.findAllByNameIn(anySet())).thenReturn(subjects);

    assertThat(service.createGroup(groupName, grade, subjectNames)).isTrue();

    verify(subjectRepository).findAllByNameIn(subjectNames);
    verify(repository).save(groupToSaveCaptor.capture());
    assertThat(groupToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedGroupToSave);
  }

  @Test
  public void createGroup_exceptionOnSave() {
    var subjectNames = Set.of("Subject 1", "Subject 2");
    var groupName = "Group";
    var grade = 123;
    var subjects = List.of(
      new Subject(UUID.randomUUID(), "Subject 1", null),
      new Subject(UUID.randomUUID(), "Subject 2", null)
    );
    var expectedGroupToSave = new Group(null, groupName, grade, Collections.emptyList(), subjects);
    var groupToSaveCaptor = ArgumentCaptor.forClass(Group.class);
    when(subjectRepository.findAllByNameIn(anySet())).thenReturn(subjects);
    when(repository.save(any())).thenThrow(new DuplicateKeyException("Exception msg"));

    assertThat(service.createGroup(groupName, grade, subjectNames)).isFalse();

    verify(subjectRepository).findAllByNameIn(subjectNames);
    verify(repository).save(groupToSaveCaptor.capture());
    assertThat(groupToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedGroupToSave);
  }

  @Test
  public void bindStudentsToGroup() {
    var groupName = "Group";
    var newStudents = List.of(
      new StudentInfo(UUID.randomUUID(), null, null),
      new StudentInfo(UUID.randomUUID(), null, null)
    );
    var newStudentInfoIds = newStudents.stream().map(StudentInfo::getId).collect(Collectors.toSet());
    var groupId = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var group = new Group(groupId, groupName, 1, new LinkedList<>(), Collections.emptyList());
    var alreadyBoundStudents = List.of(
      new StudentInfo(UUID.randomUUID(), null, group),
      new StudentInfo(UUID.randomUUID(), null, group)
    );
    group.setStudents(new LinkedList<>(alreadyBoundStudents));
    when(repository.findByName(any())).thenReturn(Optional.of(group));
    when(studentsInfoRepository.findAllByIdIn(anySet())).thenReturn(newStudents);

    assertThat(service.bindStudentsToGroup(groupName, newStudentInfoIds)).isTrue();

    assertThat(group.getStudents()).usingRecursiveComparison().isEqualTo(Stream.concat(
      alreadyBoundStudents.stream(), newStudents.stream()
    ).toList());
    assertThat(newStudents.stream().allMatch(info -> info.getGroup() == group)).isTrue();
    verify(transactionTemplate).execute(any());
    verify(repository).findByName(groupName);
    verify(studentsInfoRepository).findAllByIdIn(newStudentInfoIds);
    verify(repository).save(same(group));
  }

  @Test
  public void bindStudentsToGroup_cantFindGroup() {
    var groupName = "Group";
    var newStudentInfoIds = Set.of(UUID.randomUUID(), UUID.randomUUID());
    when(repository.findByName(any())).thenReturn(Optional.empty());

    assertThat(service.bindStudentsToGroup(groupName, newStudentInfoIds)).isFalse();

    verify(transactionTemplate).execute(any());
    verify(repository).findByName(groupName);
    verify(logger).warn("Can't find group with name='Group'");
  }

  @Test
  public void bindStudentsToGroup_exceptionOnSaveGroup() {
    var newStudents = List.of(
      new StudentInfo(UUID.randomUUID(), null, null),
      new StudentInfo(UUID.randomUUID(), null, null)
    );
    var newStudentInfoIds = newStudents.stream().map(StudentInfo::getId).collect(Collectors.toSet());
    var groupName = "Group";
    var groupId = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var group = new Group(groupId, groupName, 1, new LinkedList<>(), Collections.emptyList());
    var alreadyBoundStudents = List.of(
      new StudentInfo(UUID.randomUUID(), null, group),
      new StudentInfo(UUID.randomUUID(), null, group)
    );
    group.setStudents(new LinkedList<>(alreadyBoundStudents));
    var expectedException = new DuplicateKeyException("Exception message");
    when(repository.findByName(any())).thenReturn(Optional.of(group));
    when(studentsInfoRepository.findAllByIdIn(anySet())).thenReturn(newStudents);
    when(repository.save(any())).thenThrow(expectedException);

    assertThat(service.bindStudentsToGroup(groupName, newStudentInfoIds)).isFalse();

    verify(transactionTemplate).execute(any());
    verify(repository).findByName(groupName);
    verify(studentsInfoRepository).findAllByIdIn(newStudentInfoIds);
    verify(repository).save(same(group));
    verify(logger).error("Can't save group", expectedException);
    verify(txStatus).setRollbackOnly();
  }
}