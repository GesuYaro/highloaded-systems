package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.auth.models.DefaultUser;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;
import jakarta.annotation.Nullable;

import java.util.UUID;

public interface PeopleService {
  UUID create(DefaultUser user, PersonDto person);

  Person person(UUID id);

  @Nullable Person personByUserId(UUID userId);

  boolean update(UUID id, PersonDto person);
}
