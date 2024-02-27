package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.auth.models.DefaultUser;
import com.dedlam.ftesterlab.domain.people.models.Person;
import jakarta.annotation.Nullable;

import java.util.UUID;

public interface PeopleService {
  UUID create(DefaultUser user, PersonDto person);

  @Nullable
  Person person(UUID id);

  PeopleResponse people(int pageNumber);

  @Nullable
  Person personByUserId(UUID userId);

  boolean update(UUID id, PersonDto person);
}
