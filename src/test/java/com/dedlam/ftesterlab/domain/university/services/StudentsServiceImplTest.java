package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import com.dedlam.ftesterlab.domain.university.models.Group;
import com.dedlam.ftesterlab.domain.university.models.StudentInfo;
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

import static com.dedlam.ftesterlab.TestUtils.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentsServiceImplTest {
  private final StudentsInfoRepository studentsInfoRepository = mock();
  private final GroupsService groupsService = mock();
  private final TransactionStatus txStatus = mock();
  private final TransactionTemplate txTemplate = mock();
  private final Logger logger = mock();

  private final StudentsServiceImpl service =
    new StudentsServiceImpl(studentsInfoRepository, groupsService, txTemplate, logger);

  @BeforeEach
  public void setUp() {
    when(txTemplate.execute(any())).thenAnswer(invocation -> ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(txStatus));
  }

  @AfterEach
  public void checkNoMoreInvocations() {
    verifyNoMoreInteractions(studentsInfoRepository, groupsService, txStatus, logger);
  }

  @Test
  public void createAndInitStudentsInfo() {
    var groupName = "Group";
    var student1 = create(new Person(), p -> p.setId(UUID.randomUUID()));
    var student2 = create(new Person(), p -> p.setId(UUID.randomUUID()));
    var studentsToBind = List.of(student1, student2);
    var expectedStudentsInfoToSave = List.of(
      new StudentInfo(null, student1, null),
      new StudentInfo(null, student2, null)
    );
    var studentsInfoToSaveCaptor = ArgumentCaptor.<List<StudentInfo>>captor();
    var info1Id = UUID.randomUUID();
    var info2Id = UUID.randomUUID();
    var savedStudents = List.of(
      new StudentInfo(info1Id, student1, null),
      new StudentInfo(info2Id, student2, null)
    );
    when(studentsInfoRepository.saveAll(any())).thenReturn(savedStudents);
    when(groupsService.bindStudentsToGroup(any(), anySet())).thenReturn(true);

    assertThat(service.createAndInitStudentsInfo(groupName, studentsToBind)).isTrue();

    verify(txTemplate).execute(any());
    verify(studentsInfoRepository).saveAll(studentsInfoToSaveCaptor.capture());
    verify(groupsService).bindStudentsToGroup(groupName, Set.of(info1Id, info2Id));
    assertThat(studentsInfoToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedStudentsInfoToSave);
  }

  @Test
  public void createAndInitStudentsInfo_cantSaveInfo() {
    var groupName = "Group";
    var studentsToBind = List.of(
      create(new Person(), p -> p.setId(UUID.randomUUID())),
      create(new Person(), p -> p.setId(UUID.randomUUID()))
    );
    var saveException = new DuplicateKeyException("Can't save s-info");
    when(studentsInfoRepository.saveAll(any())).thenThrow(saveException);

    assertThat(service.createAndInitStudentsInfo(groupName, studentsToBind)).isFalse();

    verify(txTemplate).execute(any());
    verify(studentsInfoRepository).saveAll(any());
    verify(logger).error("Can't create students -> ROLLBACK", saveException);
    verify(txStatus).setRollbackOnly();
  }

  @Test
  public void createAndInitStudentsInfo_cantBindStudents() {
    var groupName = "Group";
    var student1 = create(new Person(), p -> p.setId(UUID.randomUUID()));
    var student2 = create(new Person(), p -> p.setId(UUID.randomUUID()));
    var studentsToBind = List.of(student1, student2);
    var info1Id = UUID.randomUUID();
    var info2Id = UUID.randomUUID();
    var savedStudents = List.of(
      new StudentInfo(info1Id, student1, null),
      new StudentInfo(info2Id, student2, null)
    );
    when(studentsInfoRepository.saveAll(any())).thenReturn(savedStudents);
    when(groupsService.bindStudentsToGroup(any(), anySet())).thenReturn(false);

    assertThat(service.createAndInitStudentsInfo(groupName, studentsToBind)).isFalse();

    verify(txTemplate).execute(any());
    verify(studentsInfoRepository).saveAll(any());
    verify(groupsService).bindStudentsToGroup(groupName, Set.of(info1Id, info2Id));
    verify(logger).error("Can't bind students to group -> ROLLBACK");
    verify(txStatus).setRollbackOnly();
  }

  @Test
  public void changeGroup() {
    var student1Id = UUID.randomUUID();
    var student2Id = UUID.randomUUID();
    var people = List.of(
      create(new Person(), p -> p.setId(student1Id)),
      create(new Person(), p -> p.setId(student2Id))
    );
    var studentInfo1Id = UUID.randomUUID();
    var studentInfo2Id = UUID.randomUUID();
    var studentInfos = List.of(
      create(new StudentInfo(), info -> info.setId(studentInfo1Id)),
      create(new StudentInfo(), info -> info.setId(studentInfo2Id))
    );
    when(studentsInfoRepository.findAllByStudentIdIn(anySet())).thenReturn(studentInfos);
    when(groupsService.bindStudentsToGroup(any(), anySet())).thenReturn(true);

    assertThat(service.changeGroup("Group", people)).isTrue();

    verify(studentsInfoRepository).findAllByStudentIdIn(Set.of(student1Id, student2Id));
    verify(groupsService).bindStudentsToGroup("Group", Set.of(studentInfo1Id, studentInfo2Id));
  }

  @Test
  public void removeFromGroup() {
    var student1Id = UUID.randomUUID();
    var student2Id = UUID.randomUUID();
    var people = List.of(
      create(new Person(), p -> p.setId(student1Id)),
      create(new Person(), p -> p.setId(student2Id))
    );
    var studentInfo1Id = UUID.randomUUID();
    var studentInfo2Id = UUID.randomUUID();
    var group = new Group(UUID.randomUUID(), "Group", 1, Collections.emptyList(), Collections.emptyList());
    var studentInfos = List.of(
      create(new StudentInfo(), info -> {
        info.setId(studentInfo1Id);
        info.setGroup(group);
      }),
      create(new StudentInfo(), info -> {
        info.setId(studentInfo2Id);
        info.setGroup(group);
      })
    );
    when(studentsInfoRepository.findAllByStudentIdIn(anySet())).thenReturn(studentInfos);

    assertThat(service.removeFromGroup(people)).isTrue();

    verify(studentsInfoRepository).findAllByStudentIdIn(Set.of(student1Id, student2Id));
    verify(studentsInfoRepository).saveAll(studentInfos);
    assertThat(studentInfos.stream().map(StudentInfo::getGroup).filter(Objects::nonNull).toList()).isEmpty();
  }
}