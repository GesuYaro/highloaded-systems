package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.university.models.Group;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;

import java.util.Set;
import java.util.UUID;

public interface GroupsService {
  boolean createGroup(String name, int grade, Set<String> subjectNames);

  boolean bindStudentsToGroup(String groupName, Set<UUID> studentsInfoIds);

  Window<Group> groups(OffsetScrollPosition offsetScrollPosition);
}
