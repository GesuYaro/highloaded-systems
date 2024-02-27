package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.people.models.Person;

import java.util.List;

public interface StudentsService {
  boolean createAndInitStudentsInfo(String groupName, List<Person> people);
  boolean changeGroup(String newGroupName, List<Person> people);
  boolean removeFromGroup(List<Person> people);
}
