package com.dedlam.ftesterlab.domain.university.services;

import java.util.Set;
import java.util.UUID;

public interface GroupsService {
  boolean createGroup(String name, int grade, Set<String> subjectNames);

  boolean bindStudentsToGroup(String groupName, Set<UUID> studentsInfoIds);
}
