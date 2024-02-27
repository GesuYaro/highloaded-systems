package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.models.Person;

import java.util.List;

public record PeopleResponse(
  int totalPageNumber,
  List<Person> people
) {
}
