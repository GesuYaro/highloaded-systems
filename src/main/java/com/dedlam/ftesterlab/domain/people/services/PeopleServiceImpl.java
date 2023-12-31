package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfo;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfoRepository;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class PeopleServiceImpl implements PeopleService {
  private final PeopleRepository repository;
  private final PersonContactsInfoRepository contactsInfoRepository;
  private final Logger logger;


  PeopleServiceImpl(
    PeopleRepository repository,
    PersonContactsInfoRepository contactsInfoRepository,
    Logger logger
  ) {
    this.repository = repository;
    this.contactsInfoRepository = contactsInfoRepository;
    this.logger = logger;
  }

  @Autowired
  public PeopleServiceImpl(PeopleRepository repository, PersonContactsInfoRepository contactsInfoRepository) {
    this(repository, contactsInfoRepository, LoggerFactory.getLogger(PeopleServiceImpl.class));
  }

  @Override
  public UUID create(PersonDto request) {
    var entity = new Person(null, request.getName(), request.getMiddleName(), request.getLastName(), request.getBirthday());


    try {
      Person saved = repository.save(entity);

      bindContacts(saved.getId(), Collections.emptyList());
      return saved.getId();
    } catch (RuntimeException e) {
      logger.warn("Can't create person", e);
      return null;
    }
  }

  @Override
  public List<Person> people() {
    return repository.findAll();
  }

  @Override
  public Person person(UUID id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public boolean update(UUID id, PersonDto request) {
    var notExistingPersonMsg = String.format("Can't find person by id='%s'", id);

    try {
      boolean exists = repository.existsById(id);
      if (!exists) {
        logger.warn(notExistingPersonMsg);
        return false;
      }
    } catch (RuntimeException e) {
      logger.warn(notExistingPersonMsg, e);
      return false;
    }

    try {
      var entity = new Person(id, request.getName(), request.getMiddleName(), request.getLastName(), request.getBirthday());
      repository.save(entity);
      return true;
    } catch (RuntimeException e) {
      var msg = String.format("Can't update person with id='%s'", id);
      logger.warn(msg, e);
      return false;
    }
  }

  @Override
  public void delete(UUID id) {
    boolean exists = repository.existsById(id);
    if (exists) {
      repository.deleteById(id);
    } else {
      var msg = String.format("Person with id='%s' does not exists during delete operation", id);
      logger.warn(msg);
    }
  }

  @Override
  public boolean bindContacts(UUID personId, List<Contact> contacts) {
    contacts.forEach(c -> c.setId(null));

    PersonContactsInfo contactsInfo = contactsInfoRepository.findByPerson_Id(personId);
    if(contactsInfo == null) {
      var person = new Person();
      person.setId(personId);
      contactsInfo = new PersonContactsInfo(null, person, Collections.emptyList());
    }
    var newContactsList = new LinkedList<>(contactsInfo.getContacts());
    newContactsList.addAll(contacts);
    contactsInfo.setContacts(newContactsList);

    PersonContactsInfo savedEntity = contactsInfoRepository.save(contactsInfo);

    return true;
  }

  private Page<Person> page(int pageNumber, int pageSize) {
    var pageRequest = PageRequest.of(pageNumber, pageSize);

    return repository.findAll(pageRequest);
  }

}
