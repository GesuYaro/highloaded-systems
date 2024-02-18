package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.people.database.Person;

import java.util.List;

public interface StudentsService {
  boolean createAndInitStudentsInfo(String groupName, List<Person> people);
}
