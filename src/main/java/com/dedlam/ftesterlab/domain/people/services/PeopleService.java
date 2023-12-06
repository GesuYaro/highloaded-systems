package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;

import java.util.List;
import java.util.UUID;

public interface PeopleService {
  UUID create(PersonDto person);

  List<Person> people();

  Person person(UUID id);

  boolean update(UUID id, PersonDto person);

  void delete(UUID id);

  boolean bindContacts(UUID personId, List<Contact> contacts);
}
