package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.auth.models.DefaultUser;
import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class PeopleServiceImpl implements PeopleService {
  private final PeopleRepository repository;
  private final ContactsService contactsService;
  private final Logger logger;


  public PeopleServiceImpl(PeopleRepository repository, ContactsService contactsService, Logger logger) {
    this.repository = repository;
    this.contactsService = contactsService;
    this.logger = logger;
  }

  @Autowired
  public PeopleServiceImpl(PeopleRepository repository, ContactsService contactsService) {
    this(repository, contactsService, LoggerFactory.getLogger(PeopleServiceImpl.class));
  }

  @Override
  public UUID create(DefaultUser user, PersonDto request) {
    var entity = new Person(null, request.getName(), request.getMiddleName(), request.getLastName(), request.getBirthday(), user);


    try {
      Person saved = repository.save(entity);

      contactsService.updateContacts(saved.getId(), Collections.emptyList());
      return saved.getId();
    } catch (RuntimeException e) {
      logger.warn("Can't create person", e);
      return null;
    }
  }

  @Override
  public Person person(UUID id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public Person personByUserId(UUID userId) {
    return repository.findByUserId(userId).orElse(null);
  }

  @Override
  public boolean update(UUID id, PersonDto request) {
    var notExistingPersonMsg = String.format("Can't find person by id='%s'", id);
    var existing = repository.findById(id);

    try {
      boolean exists = existing.isPresent();
      if (!exists) {
        logger.warn(notExistingPersonMsg);
        return false;
      }
    } catch (RuntimeException e) {
      logger.warn(notExistingPersonMsg, e);
      return false;
    }

    try {
      var entity = new Person(id, request.getName(), request.getMiddleName(), request.getLastName(), request.getBirthday(), existing.get().getUser());
      repository.save(entity);
      return true;
    } catch (RuntimeException e) {
      var msg = String.format("Can't update person with id='%s'", id);
      logger.warn(msg, e);
      return false;
    }
  }
}
