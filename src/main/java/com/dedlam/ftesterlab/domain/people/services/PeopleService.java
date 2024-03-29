package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.models.Person;
import jakarta.annotation.Nullable;

import java.util.UUID;

public interface PeopleService {
  @Nullable
  UUID create(UUID userId, PersonDto person);

  @Nullable
  Person person(UUID id);

  PeopleResponse people(int pageNumber);

  @Nullable
  Person personByUserId(UUID userId);

  boolean update(UUID id, PersonDto person);
}
